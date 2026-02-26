@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
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
