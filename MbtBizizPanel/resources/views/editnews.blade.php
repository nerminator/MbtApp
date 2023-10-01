@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            <div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
                <h4 style="text-align: left"><img src="../img/logo.png"></h4>
                <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
                    <li class="active"><a href="home" style="padding-left: 0px;">News</a></li>
                </ul>
                <br>
                <div class="logout">
                    <img src="../img/avatar@3x.png" style="border-radius: 100%; width: 30px;">
                    <label>{{Auth::user()->name}}</label> <br>
                    <a style="padding-left: 35px;" href="{{ route('logout') }}"
                       onclick="event.preventDefault(); document.getElementById('logout-form').submit();"><i
                                class="fa fa-sign-out"
                                aria-hidden="true"></i> Logout</a>
                    <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
                        {{ csrf_field() }}
                    </form>
                </div>
            </div>
            <form method="post" action="{{url('editnews')}}">
                {{ csrf_field() }}
                <div class="col-md-9" style="margin-top: 15px;">
                    <div class="col-md-9"><h3 style="margin-top: 10px;">News <i class="fa fa-caret-right"
                                                                                aria-hidden="true"></i>
                            Edit News</h3>
                    </div>
                    <div class="col-md-3">
                        {{--<div class="col-md-5">
                            <a href="home" class="col-md-12" style="padding-top: 12px;">İptal</a>
                        </div>
                        <div class="col-md-7">
                            <button type="submit" class="col-md-12 btn btn-primary">Kaydet</button>
                        </div>--}}
                    </div>
                    <div class="col-md-12" style="margin-top: 25px;">
                        @if (session('status'))
                            <div class="alert alert-danger">
                                {{ session('status') }}
                            </div>
                        @endif
                        <input type="hidden" id="news_id" value="{{$id}}" name="id">
                        <div id="news_detail">
                            <div class="col-md-12" style="padding: 0px">
                                <div class="col-md-6">
                                    <label class="col-md-12 translation_title" for="type">TYPE</label>
                                    <select name="type" class="col-md-12 form-control" style="height: 45px;"
                                            onchange="showDiscType()">
                                        <option value="2">Event</option>
                                        <option value="3">Discounts</option>
                                        {{--<option value="4">Farewell</option>--}}
                                        {{--<option value="5">Death</option>--}}
                                        <option value="7">News</option>
                                        <option value="8">Useful Links</option>
                                        <option value="9">Contacts</option>
                                        <option value="6">Other</option>
                                    </select>
                                    <div name="discountTypeDiv">
                                    </div>
                                    <label class="col-md-12 translation_title" for="listText">LIST TEXT -
                                        TURKISH</label>
                                    <input type="text" class="col-md-12 form-control"
                                           placeholder="Add List Text" name="listText" required
                                           oninvalid="this.setCustomValidity('Please enter a list text')"
                                           oninput="setCustomValidity('')" maxlength="100">
                                    <label class="col-md-12 translation_title" for="listTextEn">LIST TEXT -
                                        ENGLISH</label>
                                    <input type="text" class="col-md-12 form-control"
                                           placeholder="Add List Text" name="listTextEn" required
                                           oninvalid="this.setCustomValidity('Please enter a list text')"
                                           oninput="setCustomValidity('')" maxlength="100">
                                    <div id="textDiv">
                                        <label class="col-md-12 translation_title" for="title">TITLE - TURKISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                               placeholder="Add Title" name="title" maxlength="100">
                                        <label class="col-md-12 translation_title" for="titleEn">TITLE - ENGLISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                               placeholder="Add Title" name="titleEn" maxlength="100">
                                        <label class="col-md-12 translation_title" for="text">TEXT - TURKISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                                  placeholder="Add Text"
                                                  name="text" maxlength="10000"></textarea>
                                        <label class="col-md-12 translation_title" for="textEn">TEXT - ENGLISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                                  placeholder="Add Text"
                                                  name="textEn" maxlength="10000"></textarea>
                                        <label class="col-md-12 translation_title" for="subTitle">SUBTITLE -
                                            TURKISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                               placeholder="Add Subtitle" name="subTitle" maxlength="100">
                                        <label class="col-md-12 translation_title" for="subTitleEn">SUBTITLE -
                                            ENGLISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                               placeholder="Add Subtitle" name="subTitleEn" maxlength="100">
                                        <label class="col-md-12 translation_title" for="subText">SUBTEXT -
                                            TURKISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                                  placeholder="Add Subtext"
                                                  name="subText" maxlength="10000"></textarea>
                                        <label class="col-md-12 translation_title" for="subTextEn">SUBTEXT -
                                            ENGLISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                                  placeholder="Add Subtext"
                                                  name="subTextEn" maxlength="10000"></textarea>
                                    </div>
                                    <label class="col-md-12 translation_title" for="url">URL</label>
                                    <input type="url" class="col-md-12 form-control"
                                           placeholder="Add URL" name="url"
                                           oninvalid="this.setCustomValidity('Please enter a valid URL. Protocol is required. (http:// or https://)')"
                                           oninput="setCustomValidity('')">
                                    <div id="phoneNumber">
                                        <label class="col-md-12 translation_title" for="phone">PHONE</label>
                                        <input type="number" class="col-md-12 form-control"
                                               placeholder="Add Phone Number" name="phone"
                                               oninput="setCustomValidity('')">
                                    </div>
                                    <label class="col-md-12 translation_title" for="employeeType">EMPLOYEE TYPE</label>
                                    <select name="employeeType" class="col-md-12 form-control" style="height: 45px;">
                                        <option value="">-</option>
                                        @foreach($emp as $item)
                                            <option value="{{ $item['value'] }}">{{ $item['text'] }}</option>
                                        @endforeach
                                    </select>
                                    <label class="col-md-12 translation_title" for="locationIdList">COMPANY
                                        LOCATION</label>
                                    <select name="locationIdList[]" class="col-md-12 form-control"
                                            style="height: 45px;" multiple>
                                        @foreach($compL as $item)
                                            <option value="{{ $item['value'] }}">{{ $item['text'] }}</option>
                                        @endforeach
                                    </select>
                                    <label class="col-md-12 translation_title" for="companyIdList">COMPANY CODE</label>
                                    <select name="companyIdList[]" class="col-md-12 form-control"
                                            style="height: 45px;" multiple>
                                        @foreach($compC as $item)
                                            <option value="{{ $item['value'] }}">{{ $item['text'] }}</option>
                                        @endforeach
                                    </select>
                                </div>
                                <a style="color: #636b6f;" name="add_cover">
                                    <div id="AddCoverPhotoDiv" class="col-md-3"
                                         style="text-align:center; border: 1px solid gray; min-height: 180px; margin-top: 40px; background-color: lightgray; padding-top: 18px;"
                                         data-toggle="modal" data-target="#coverphoto_modal">
                                        <br>
                                        <label style="font-size: 40px; height: 40px;">+</label> <br>
                                        <label>Add Cover Photo</label>
                                    </div>
                                    <div id="CoverPhotoDiv" hidden class="col-md-3"
                                         style="padding:0px; text-align:center; border: 1px solid gray; margin-top: 40px;">
                                        <img src="" id="CoverPhoto" class="col-md-12" style="padding: 0px;"
                                             data-toggle="modal" data-target="#coverphoto_modal">
                                        <a class="col-md-12 btn btn-default"
                                           style="height: 43px; margin-top: -2px; padding-top: 10px;"
                                           name="deleteimageinedit">Delete Image</a>
                                        <input type="hidden" name="imageString">
                                    </div>
                                </a>
                            </div>
                            <div class="col-md-12" style="padding: 0px;">
                                <div class="col-md-6">
                                    <div class="col-md-6" style="padding: 0px;">
                                        <label class="col-md-12 translation_title" for="startTime">START DATE</label>
                                        <div class="col-md-12" style="padding-left: 0px;">
                                            <input type="datetime-local" class="col-md-12 form-control"
                                                   name="startTime" required
                                                   oninvalid="this.setCustomValidity('Please enter a date')"
                                                   oninput="setCustomValidity('')"
                                                   style="padding-left:5px; padding-right: 5px">
                                        </div>
                                    </div>
                                    <div class="col-md-6" style="padding: 0px;">
                                        <div class="col-md-12" style="padding-right: 0px;">
                                            <label class="col-md-12 translation_title" for="endTime">END DATE
                                                (OPTIONAL)</label>
                                            <input type="datetime-local" class="col-md-12 form-control"
                                                   name="endTime"
                                                   style="padding-left:5px; padding-right: 5px">
                                        </div>
                                    </div>
                                </div>
                                {{--<div class="col-md-3" style="padding:0px;"><br><br>
                                    <a class="col-md-12 btn btn-default"
                                       style="height: 43px; margin-top: -2px; padding-top: 10px;"
                                       name="surveymonkey_notif">SEND NOTIFICATION</a>
                                </div>--}}
                            </div>
                        </div>
                        <div class="col-md-3">
                            <br>
                            <button type="submit" class="col-md-12 btn btn-primary">Save</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        <div id="coverphoto_modal" class="modal fade in" role="dialog">
            <div class="modal-dialog modal-lg">
                <form id="coverform" action="javascript: void(0)">
                    <div class="modal-content" style="min-height: 500px;">
                        <div class="modal-header" style="border-bottom: 0px;">
                            <h4 class="modal-title col-md-8">Kapak Fotoğrafı <br>
                            </h4>
                            <div class="col-md-4">
                                <div class="col-md-5">
                                    <a class="col-md-12" data-dismiss="modal" style="padding-top: 12px;">Kapat</a>
                                </div>
                                <div class="col-md-7">
                                    <button id='savecoverphoto' data-dismiss="modal" class="col-md-12 btn btn-primary"
                                            style="padding-top: 7px;">
                                        Kaydet
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="modal-body"
                             style="display: inline-block; overflow-y: auto; max-height: 600px; width: 100%;">
                            <div class="image-editor">
                                <input type="file" name="cover_photo" accept="image/*" class="cropit-image-input">
                                <br>
                                <div class="cropit-preview" style="width: 470px; height:336px; cursor: move"></div>
                                <br>
                                <div class="image-size-label">
                                    Size
                                </div>
                                <br>
                                <div class="col-md-6" style="padding: 0">
                                    <input type="range" class="cropit-image-zoom-input">
                                </div>
                                <input type="hidden" name="image-data" class="hidden-image-data"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
@endsection
