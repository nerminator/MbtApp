#!/usr/bin/env ruby
# encoding: utf-8
#
# Creates MBTBordro-Staging and MBTBordro-Production targets
# by duplicating MBTBiziz-Staging and MBTBiziz-Production.
#
# Usage:
#   GEM_HOME="/opt/homebrew/Cellar/cocoapods/1.16.2_1/libexec" \
#   /opt/homebrew/opt/ruby/bin/ruby create_bordro_targets.rb
#

require 'xcodeproj'
require 'fileutils'

PROJECT_PATH = File.join(__dir__, 'MBTBiziz.xcodeproj')
project = Xcodeproj::Project.open(PROJECT_PATH)

TARGETS_TO_CREATE = [
  {
    source: 'MBTBiziz-Staging',
    name:   'MBTBordro-Staging',
    bundle_id: 'com.daimlertruck.dtag.internal.ios.mbt.bordro.test2',
    display_name: 'MBT Bordro Staging',
    entitlements: 'MBTBiziz/MBTBiziz.entitlements',
    sign_style: 'Automatic',
    sign_identity: 'Apple Development',
    provisioning: '',
  },
  {
    source: 'MBTBiziz-Production',
    name:   'MBTBordro-Production',
    bundle_id: 'com.daimlertruck.dtag.internal.ios.mbt.bordro.test',
    display_name: 'MBT Bordro',
    entitlements: 'MBTBiziz-Prod.entitlements',
    sign_style: 'Manual',
    sign_identity: 'iPhone Distribution',
    provisioning: '',  # Update with your provisioning profile name
  },
]

def duplicate_target(project, config)
  source = project.targets.find { |t| t.name == config[:source] }
  raise "Source target '#{config[:source]}' not found!" unless source

  # Check if target already exists
  if project.targets.any? { |t| t.name == config[:name] }
    puts "  ⚠  Target '#{config[:name]}' already exists, skipping."
    return project.targets.find { |t| t.name == config[:name] }
  end

  puts "  Creating target: #{config[:name]} (from #{config[:source]})"

  # Create the new native target
  new_target = project.new_target(
    :application,
    config[:name],
    :ios,
    '14.0'
  )

  # Remove default build phases created by new_target (we'll copy from source)
  new_target.build_phases.each(&:remove_from_project)

  # Copy build phases from source
  source.build_phases.each do |phase|
    new_phase = case phase
    when Xcodeproj::Project::Object::PBXSourcesBuildPhase
      new_target.new_shell_script_build_phase('dummy') # placeholder
      new_target.build_phases.last.remove_from_project
      bp = project.new(Xcodeproj::Project::Object::PBXSourcesBuildPhase)
      new_target.build_phases << bp
      bp
    when Xcodeproj::Project::Object::PBXResourcesBuildPhase
      bp = project.new(Xcodeproj::Project::Object::PBXResourcesBuildPhase)
      new_target.build_phases << bp
      bp
    when Xcodeproj::Project::Object::PBXFrameworksBuildPhase
      bp = project.new(Xcodeproj::Project::Object::PBXFrameworksBuildPhase)
      new_target.build_phases << bp
      bp
    when Xcodeproj::Project::Object::PBXShellScriptBuildPhase
      bp = new_target.new_shell_script_build_phase(phase.name || 'Run Script')
      bp.shell_script = phase.shell_script
      bp.shell_path = phase.shell_path
      bp.input_paths = phase.input_paths.to_a
      bp.output_paths = phase.output_paths.to_a
      bp.input_file_list_paths = phase.input_file_list_paths.to_a if phase.respond_to?(:input_file_list_paths)
      bp.output_file_list_paths = phase.output_file_list_paths.to_a if phase.respond_to?(:output_file_list_paths)
      bp.show_env_vars_in_log = phase.show_env_vars_in_log if phase.respond_to?(:show_env_vars_in_log)
      bp
    when Xcodeproj::Project::Object::PBXHeadersBuildPhase
      bp = project.new(Xcodeproj::Project::Object::PBXHeadersBuildPhase)
      new_target.build_phases << bp
      bp
    when Xcodeproj::Project::Object::PBXCopyFilesBuildPhase
      bp = project.new(Xcodeproj::Project::Object::PBXCopyFilesBuildPhase)
      bp.name = phase.name
      bp.dst_path = phase.dst_path
      bp.dst_subfolder_spec = phase.dst_subfolder_spec
      new_target.build_phases << bp
      bp
    else
      puts "    Skipping unknown build phase type: #{phase.class}"
      next
    end

    # Copy file references for Sources, Resources, Frameworks, Headers, CopyFiles
    if phase.respond_to?(:files) && new_phase.respond_to?(:add_file_reference)
      phase.files.each do |build_file|
        next unless build_file.file_ref
        begin
          new_build_file = new_phase.add_file_reference(build_file.file_ref)
          new_build_file.settings = build_file.settings.dup if build_file.settings
        rescue => e
          # Skip duplicates
        end
      end
    end
  end

  # Copy build configurations
  new_target.build_configurations.each do |new_config|
    source_config = source.build_configurations.find { |c| c.name == new_config.name }
    next unless source_config

    new_config.build_settings = Marshal.load(Marshal.dump(source_config.build_settings))

    # Override target-specific settings
    new_config.build_settings['PRODUCT_BUNDLE_IDENTIFIER'] = config[:bundle_id]
    new_config.build_settings['INFOPLIST_KEY_CFBundleDisplayName'] = config[:display_name]
    new_config.build_settings['PRODUCT_NAME'] = '$(TARGET_NAME)'
    new_config.build_settings['CODE_SIGN_ENTITLEMENTS'] = config[:entitlements]
    new_config.build_settings['CODE_SIGN_STYLE'] = config[:sign_style]
    new_config.build_settings['CODE_SIGN_IDENTITY'] = config[:sign_identity]
    new_config.build_settings['PROVISIONING_PROFILE_SPECIFIER'] = config[:provisioning]
    new_config.build_settings['SWIFT_OBJC_INTERFACE_HEADER_NAME'] = "#{config[:name].gsub('-', '_')}-Swift.h"

    # Copy base configuration reference (Pods xcconfig) - will be updated by pod install
    new_config.base_configuration_reference = source_config.base_configuration_reference
  end

  puts "    ✓ Target created with #{new_target.build_phases.count} build phases"
  new_target
end

def create_scheme(project, target, source_scheme_name)
  scheme_dir = File.join(PROJECT_PATH, 'xcshareddata', 'xcschemes')
  FileUtils.mkdir_p(scheme_dir)

  source_scheme_path = File.join(scheme_dir, "#{source_scheme_name}.xcscheme")
  new_scheme_path = File.join(scheme_dir, "#{target.name}.xcscheme")

  if File.exist?(new_scheme_path)
    puts "    ⚠  Scheme '#{target.name}' already exists, skipping."
    return
  end

  if File.exist?(source_scheme_path)
    content = File.read(source_scheme_path)
    # Replace target references
    source_target = project.targets.find { |t| t.name == source_scheme_name }
    content = content.gsub(source_target.uuid, target.uuid)
    content = content.gsub("#{source_scheme_name}.app", "#{target.name}.app")
    content = content.gsub("BlueprintName = \"#{source_scheme_name}\"",
                           "BlueprintName = \"#{target.name}\"")
    File.write(new_scheme_path, content)
    puts "    ✓ Scheme created: #{target.name}.xcscheme"
  else
    puts "    ⚠  Source scheme not found, creating minimal scheme..."
    scheme = Xcodeproj::XCScheme.new
    scheme.add_build_target(target)
    scheme.set_launch_target(target)
    scheme.save_as(PROJECT_PATH, target.name, true)
    puts "    ✓ Scheme created: #{target.name}.xcscheme"
  end
end

# ── Main ────────────────────────────────────────────────────────────────────
puts "\n━━━ Creating Bordro Targets ━━━\n\n"

new_targets = []
TARGETS_TO_CREATE.each do |config|
  target = duplicate_target(project, config)
  new_targets << { target: target, config: config }
end

project.save
puts "\n  ✓ Project saved.\n"

# Create schemes
puts "\n━━━ Creating Schemes ━━━\n\n"
new_targets.each do |entry|
  create_scheme(project, entry[:target], entry[:config][:source])
end

puts "\n━━━ Done! ━━━"
puts "\nNext steps:"
puts "  1. Update Podfile to add new targets"
puts "  2. Run: pod install"
puts "  3. Open .xcworkspace in Xcode to verify"
puts ""
