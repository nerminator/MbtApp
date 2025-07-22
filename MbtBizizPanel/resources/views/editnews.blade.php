@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            <div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
                <h4 style="text-align: left"><img src="../img/logo4.png" style="margin-left: -42px;"></h4>
                <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
                    <li class="active"><a href="home" style="padding-left: 0px;">News</a></li>
                    <li><a href="socialclubs" style="padding-left: 0px;">Social Clubs</a></li>
                    <li><a href="phones" style="padding-left: 0px;">Phone Numbers</a></li>
                    <li><a href="medias" style="padding-left: 0px;">Social Media</a></li>
                    <li><a href="feedback" style="padding-left: 0px;">App Feedback</a></li>
                    <li><a href="aboutus" style="padding-left: 0px;">About Us</a></li>
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
            <form id="newsForm" method="post" onsubmit="onClickSubmitForm();return false;">
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
                        <input type="hidden" id="newsData" value="{{json_encode($newsData)}}" name="newsData">

                        <div id="news_detail">
                            <div class="col-md-12" style="padding: 0px">
                                <div class="col-md-6">
                                    <div id="typeDiv">
                                        <label class="col-md-12 translation_title" for="type">TYPE</label>
                                
                                        <select name="type" class="col-md-12 form-control" style="height: 45px;"
                                                onchange="showDiscType()">
                                            <option value="7">News</option>
                                            <option value="2">Event</option>
                                            <option value="3">Discounts</option>
                                            {{--<option value="4">Farewell</option>--}}
                                            {{--<option value="5">Death</option>--}}
                                            <option value="8">Useful Links</option>
                                            <option value="9">Contacts</option>
                                            <option value="10">Social Club</option>
                                            <option value="6">Other</option>
                                        </select>
                                    </div>
                                    <div name="discountTypeDiv"></div>
                                    
                                    <div id="textDiv">
                                        <label class="col-md-12 translation_title" for="title">TITLE - TURKISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                            value="{{$newsData->title}}" placeholder="Add Title" name="title" maxlength="150">
                                        <label class="col-md-12 translation_title" for="titleEn">TITLE - ENGLISH</label>
                                        <input type="text" class="col-md-12 form-control"
                                            value="{{$newsData->title_en}}" placeholder="Add Title" name="titleEn" maxlength="150">
                                        <label class="col-md-12 translation_title" for="text">TEXT - TURKISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                            placeholder="Add Text"
                                            name="text" maxlength="10000">{{$newsData->text}}</textarea>
                                        <label class="col-md-12 translation_title" for="textEn">TEXT - ENGLISH</label>
                                        <textarea class="col-md-12 form-control" rows="5"
                                            placeholder="Add Text"
                                            name="textEn" maxlength="10000">{{$newsData->text_en}}</textarea>
                                    </div>
                                    <label class="col-md-12 translation_title" for="url">URL</label>
                                    <input type="url" class="col-md-12 form-control"
                                           value="{{$newsData->url}}" placeholder="Add URL" name="url"
                                           oninvalid="this.setCustomValidity('Please enter a valid URL. Protocol is required. (http:// or https://)')"
                                           oninput="setCustomValidity('')">
                                    <div id="phoneNumber">
                                        <label class="col-md-12 translation_title" for="phone">PHONE</label>
                                        <input type="number" class="col-md-12 form-control"
                                            value="{{$newsData->phone}}" placeholder="Add Phone Number" name="phone"
                                            oninput="setCustomValidity('')">
                                    </div>
                                    @if ($newsData->type != 10) 
                                        <label class="col-md-12 translation_title" for="employeeType">EMPLOYEE TYPE</label>
                                        <select name="employeeType" 
                                            value="{{$newsData->employee_type}}" class="col-md-12 form-control" style="height: 45px;">
                                            <option value="">-</option>
                                            @foreach($emp as $item)
                                                <option value="{{ $item['value'] }}">{{ $item['text'] }}</option>
                                            @endforeach
                                        </select>
                                    @else<div hidden>
                                        <label class="col-md-12 translation_title" for="locationIdList">COMPANY LOCATION</label>
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
                                    @endif
                                </div>
                                <div class="col-md-6">
                                <label class="col-md-12 translation_title" for="documents">IMAGES </label>

                                <div class="row">
                                    <a class="btn btn-default" data-toggle="modal" data-target="#coverphoto_modal"
                                    style="height: 43px; margin-left:16px; margin-top: -2px; padding-top: 10px;"
                                    name="addImage">Add Image</a>   
                                    
                                    <a id="DeleteImageButton" class="btn btn-default"
                                    style="display:none; height: 43px; width:140px; margin-top: -2px; padding-top: 10px;"
                                    name="deleteimageinedit">Delete Image</a>        
                                </div>
                                <div class="row" id="SliderDiv" hidden>                              
                                <section id="SliderSection" class="splide"  aria-label="Splide Basic HTML Example" style="float:left; margin-top:5px; width:380px">
                                    <div class="splide__track">
                                            <ul id="EditNewsImages"class="splide__list">
                                            </ul>                                                                                                                                                                                                                                                                                                                                                                                                  
                                    </div>
                                </section>                              
                                </div>

                                <div  class="row">
                                    <div id="documents">
                                        <div class="col-md-12" style="padding: 0px">
                                            <div class="col-md-6">
                                                <label class="col-md-12 translation_title" for="documents">PDF DOCUMENTS</label>
                                                <div class="col-md-6" style="padding-left: 0px;">
                                                    <div hidden name="pdf_inputfile">
                                                    </div>
                                                    <a class="col-md-12 btn btn-default"
                                                    style="height: 43px; width:140px; padding-top: 10px;" name="upload_document">
                                                        <i class="fa fa-upload" aria-hidden="true"></i> Dosya Yükle</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12" name="pdf_list" hidden>
                                            <div class="col-md-12" style="margin-top: 20px;">
                                                <div class="col-md-10 col-md-offset-1"></div>
                                            </div>
                                        </div>                                   
                                    </div>
                                </div>

                                <div  class="row" style="margin-top: 50px;">
                                    <div id="discounts_code_div" class="discounts_code_div" hidden>
                                        <div>
                                            <label class="col-md-12 translation_title" for="type">DISCOUNT CODE TYPE</label>
                                            <select name="discountCodeType" class="col-md-6 form-control" style="height: 45px;"
                                                    onchange="showDiscCodeType()">
                                                <option value="0">No Code</option>
                                                <option value="1">One code for all</option>
                                                <option value="2">One code for each person</option>

                                            </select>
                                        </div>
                                        <div id="one_for_all_div"  class="col-md-12" style="padding: 0px" hidden>
                                        
                                            <label class="col-md-12 translation_title" for="subTitle">DISCOUNT CODE</label>
                                            <input type="text" class="col-md-12 form-control"
                                                placeholder="Add Discount Code" name="oneForAllCodeInput" maxlength="120">
                                        </div>

                                        <div id="one_for_each_div"  class="col-md-12" style="padding: 0px" hidden>
                                            
                                            <div class="col-md-10" style="padding-left: 0px;">
                                                <div hidden name="discount_inputfile">
                                                    <input type='file' id='discountCodeFile' name='discountCodeFile' accept='application/txt' onchange='discountCodeFileSelected(this);' hidden>
                                                </div>
                                                <a class="col-md-12 btn btn-default"
                                                style="height: 43px; width:240px; padding-top: 10px; margin-top : 12px;" name="upload_discountcodes_button">
                                                    <i class="fa fa-upload" aria-hidden="true"></i> Upload File with Discount Codes</a>
                                            </div>
                                            
                                            <div class="col-md-12" id="discount_code_metrics" hidden>
                                                <div class="row">
                                                    <label class="col-md-6 translation_title" for="subTitle">Number of Discount Codes:</label>
                                                    <label id="discount_code_count" class="col-md-6 translation_title" for="subTitle">0</label>
                                                </div>
                                                <div class="row">
                                                    <label class="col-md-6 translation_title" for="subTitle">Used Discount Codes:</label>
                                                    <label id="discount_code_used" class="col-md-6 translation_title" for="subTitle">0</label>
                                                </div>
                                            </div>

                                        </div>
                                 
                                    </div>
                                </div>

                                
                                </div>
                                <input type="hidden" name="imageString" value="">
                                <input type="hidden" name="imageString1" value="">
                                <input type="hidden" name="imageString2" value="">
                                <input type="hidden" name="imageString3" value="">
                                <input type="hidden" name="imageString4" value="">
                                <input type="hidden" name="imageString5" value="">
                                <input type="hidden" name="imageString6" value="">
                                <input type="hidden" name="imageString7" value="">
                                <input type="hidden" name="imageString8" value="">   
                                <input type="hidden" name="imageString9" value="">
                            </div>

                            <div class="col-md-12" style="padding: 0px;" id="dateDiv">
                                <div class="col-md-6">
                                    <div class="col-md-6" style="padding: 0px;">
                                        <label class="col-md-12 translation_title" for="startTime">START DATE</label>
                                        <div class="col-md-12" style="padding-left: 0px;">
                                            <input type="datetime-local" class="col-md-12 form-control"
                                                   name="startTime" 
                                                   oninvalid="this.setCustomValidity('Please enter a date')"
                                                   oninput="setCustomValidity('')"
                                                   style="padding-left:5px; padding-right: 5px" id="startTime_">
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
                            <button id="submitForm" type="submit" class="col-md-12 btn btn-primary">Save</button>
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
