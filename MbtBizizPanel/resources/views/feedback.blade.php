@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            <div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
                <h4 style="text-align: left"><img src="../img/logo4.png" style="margin-left: -42px;"></h4>
                <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
                    <li><a href="home" style="padding-left: 0px;">News</a></li>
                    <li><a href="socialclubs" style="padding-left: 0px;">Social Clubs</a></li>
                    <li><a href="phones" style="padding-left: 0px;">Phone Numbers</a></li>
                    <li><a href="medias" style="padding-left: 0px;">Social Media</a></li>
                    <li class="active"><a href="feedback" style="padding-left: 0px;">App Feedback</a></li>
                    <li><a href="aboutus" style="padding-left: 0px;">About Us</a></li>
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
                <div class="row" id = "emailDiv">
                            <label class="col-md-3" for="text">SEND EMAIL TO</label>
                            <textarea name="emails" class="col-md-9 form-control" rows="1"
                                                  placeholder="Add emails with comma seperator"
                                                  name="emailsText" maxlength="2000"
                                                  onchange="onChangeEmails(this);">{{ $emails}}</textarea>
                </div>
                <div class="row" id = "feedbackDiv" style="margin-top: 30px;">
                    <label class="col-md-12" for="text">FEEDBACKS</label>
                    @foreach($feedbacks as $feedback)
                        <div class="col-md-12 eventdiv passive">
                            <label>Posted by : </label> <label>{{ $feedback['user']}} </label> 
                            <label style="float:right">Date : {{ $feedback['created_at']}}</label>
                            <br>
                            <label>{{ $feedback['text']}}</label>
                            <br>
                        </div>
                    @endforeach
                    <br>
                    <a id="loadmore" href="javascript:onClickLoadMore();" >Load More </button>
                </div>
            </div>
        </div>
    </div>
@endsection
