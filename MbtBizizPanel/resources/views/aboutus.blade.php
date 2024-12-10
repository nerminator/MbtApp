@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            <div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
                <h4 style="text-align: left"><img src="../img/log3.png"></h4>
                <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
                    <li><a href="home" style="padding-left: 0px;">News</a></li>
                    <li><a href="socialclubs" style="padding-left: 0px;">Social Clubs</a></li>
                    <li><a href="phones" style="padding-left: 0px;">Phone Numbers</a></li>
                    <li><a href="medias" style="padding-left: 0px;">Social Media</a></li>
                    <li><a href="feedback" style="padding-left: 0px;">App Feedback</a></li>
                    <li class="active"><a href="aboutus" style="padding-left: 0px;">About Us</a></li>
                    <li><a href="about" style="padding-left: 0px;">About</a></li>
                    <li><a href="appDescription" style="padding-left: 0px;">App Description</a></li>                    
                </ul>
                <br>
                <div class="logout">
                    <img src="../img/avatar@3x.png" style="border-radius: 100%; width: 30px;">
                    <label>{{Auth::user()->name}}</label> <br>
                    <a style="padding-left: 35px;" href="{{ route('logout') }}"
                       onclick="event.preventDefault(); document.getElementById('logout-form').submit();"><i
                                class="fa fa-sign-out"
                                aria-hidden="true"></i> Logout</a>
                    <form id="logout-form" action="{{ route('logout') }}" method="GET" style="display: none;">
                        {{ csrf_field() }}
                    </form>
                </div>
            </div>
            <div class="col-md-9" style="margin-top: 15px;">
                <form method="post" action="{{url('updateAboutus')}}">
                    @csrf
                    <h3 style="margin-top: 10px;">About Us TR</h3>
                    <textarea id="aboutusTRHtml" name="aboutusTRHtml" style="width:100%;margin-left: 12px;background: dimgray;
    height: 300px;">{{ $contentTR }}</textarea>
                    <h3 style="margin-top: 10px;">About Us EN</h3>
                    <textarea id="aboutusENHtml" name="aboutusENHtml" style="width:100%;margin-left: 12px;background: dimgray;
    height: 300px;">{{ $contentEN }}</textarea>
                    <br>
                    <div class="col-md-3">
                        <br>
                        <button type="submit" class="col-md-12 btn btn-primary">Save</button>
                    </div>
                </form>

            </div>
        </div>
    </div>
@endsection
