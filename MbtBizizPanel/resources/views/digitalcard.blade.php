@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
            <div class="col-md-9" style="margin-top: 15px;">

                <h3 style="margin-bottom: 30px;">Digital Business Card Settings</h3>

                @if(session('success'))
                    <div class="alert alert-success">{{ session('success') }}</div>
                @endif

                <form action="{{ url('/digitalcard/save') }}" method="POST">
                    @csrf

                    <label class="col-md-12 translation_title">VIDEO URL</label>
                    <input type="text" 
                        class="col-md-12 form-control"
                        name="video_url"
                        value="{{ $videoUrl }}"
                        placeholder="https://youtube.com/...">

                    <br><br>

                    <label class="col-md-12 translation_title">LINKEDIN URL</label>
                    <input type="text"
                        class="col-md-12 form-control"
                        name="linkedin_url"
                        value="{{ $linkedinUrl }}"
                        placeholder="https://linkedin.com/...">

                    <br><br>

                    <button type="submit" class="btn btn-primary" style="margin-top: 30px;">Save</button>
                </form>

            </div>

        </div>
    </div>
@endsection