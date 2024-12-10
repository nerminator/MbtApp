
package com.daimlertruck.dtag.internal.android.mbt.test.network.entity.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileEntity {

    /*    @Expose
        private Integer employeeType;*/
    @Expose
    @SerializedName("com/daimlertruck/dtag/internal/android/mbt/test/manager")
    private String manager;
    @Expose
    @SerializedName("nameSurname")
    private String nameSurname;
    @Expose
    @SerializedName("officeLocation")
    private String officeLocation;
    @Expose
    @SerializedName("organizationUnit")
    private String organizationUnit;
    @Expose
    @SerializedName("registerNumber")
    private String registerNumber;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("workHoursText")
    private String workHoursText;
    @Expose
    @SerializedName("workHoursAvailable")
    private Boolean workHoursAvailable;

    @Expose
    @SerializedName("yearlyWorkHoursText")
    private String yearlyWorkHoursText;

    @Expose
    @SerializedName("monthlyWorkHoursText")
    private String monthlyWorkHoursText;


  /*  public Integer getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(Integer employeeType) {
        this.employeeType = employeeType;
    }*/

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkHoursText() {
        return workHoursText;
    }

    public Boolean getWorkHoursAvailable() {
        return workHoursAvailable;
    }

    public String getYearlyWorkHoursText() {
        return yearlyWorkHoursText;
    }

    public void setYearlyWorkHoursText(String yearlyWorkHoursText) {
        this.yearlyWorkHoursText = yearlyWorkHoursText;
    }

    public String getMonthlyWorkHoursText() {
        return monthlyWorkHoursText;
    }

    public void setMonthlyWorkHoursText(String monthlyWorkHoursText) {
        this.monthlyWorkHoursText = monthlyWorkHoursText;
    }
}
