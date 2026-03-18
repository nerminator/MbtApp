@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
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
