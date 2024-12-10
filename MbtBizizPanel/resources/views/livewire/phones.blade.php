
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            <div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
                <h4 style="text-align: left"><img src="../img/logo4.png" style="margin-left: -42px;"></h4>
                <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
                    <li><a href="home" style="padding-left: 0px;">News</a></li>
                    <li><a href="socialclubs" style="padding-left: 0px;">Social Clubs</a></li>
                    <li class="active"><a href="phones" style="padding-left: 0px;">Phone Numbers</a></li>
                    <li><a href="medias" style="padding-left: 0px;">Social Media</a></li>
                    <li><a href="feedback" style="padding-left: 0px;">App Feedback</a></li>
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
            <div id = "phonesDiv" class="col-md-9" style="margin-top: 15px;">
                <div class="row" style="margin-top: 30px;">
                    <a id="addNewPhoneLoc" style="margin: 18px;display: inline-block;" href="javascript:onClickAddPhoneLoc(this);" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Phone Numbers Location</a>
                    <div id="phoneLocsDiv">
                        @foreach($phone_locations as $loc)
                            <div id="locDiv-{{ $loc['id'] }}">
                                <div class="row sc_collapsible sc_active" style="padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;" >
                                    <div class="col-md-12" style="width: calc(100% - 18px); padding-right: 0px; padding-left:0px;">
                                        <div class="col-md-11" style="padding-left:0px;">
                                            <input type="text" class="sc-input-text" placeholder="ex: Aksaray" maxlength="100" data="{{ $loc['id'] }}" value="{{ $loc['location'] }}" onchange="onChangePhoneLoc(this);">
                                        </div>

                                        <div class="dropdown col-md-1" style="text-align: right;">
                                            <a style="text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown" onclick="event.preventDefault();"></a>
                                            <ul class="dropdown-menu">
                                                <li><a class="remove" href="javascript:onClickDeletePhoneLoc({{ $loc['id'] }});"><b>Delete</b></a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>

                                <div class="content sc1_content" style="display: block;">
                                    <div class="row" style="margin-left: 18px;">
                                        <label  for="text">Santral Numarası</label>
                                        <input type="text" class="sc-input-text" style="margin-left: 20px;width: 100px; height:25px; display:inline-block;"
                                        placeholder="" name="santral" maxlength="7"
                                        data="{{ $loc['id'] }}" 
                                        value="{{ $loc['santral'] }}"
                                        onchange="onChangeSantral(this);">
                                    </div>
                                    <a style="margin: 18px;display: inline-block;" href="javascript:onClickAddPhone({{ $loc['id'] }});" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Phone</a>
                                    <div id="phonesDivForLoc-{{ $loc['id'] }}" wire:sortable="updatePhoneOrder">
                                        @foreach($loc['phones'] as $phone)
                                        <div id="phoneDiv-{{ $phone['id'] }}" wire:sortable.item="{{ $phone['id']}}" wire:key="phone-{{ $phone['id']}}">
                                            <div class="row sc2_collapsible" style="padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;" >
                                                <div class="col-md-12" style="width: calc(100% - 18px); padding-right: 0px; padding-left:0px;">
                                                    <div class="col-md-10" style="padding-left:0px;">
                                                        <input type="text" class="sc-input-text" placeholder="ex: İtfaiye" maxlength="100" data="{{ $phone['id'] }}" value="{{ $phone['name'] }}" onchange="onChangePhone(this);">
                                                    </div>
                                                    <div class="col-md-1" wire:sortable.handle style="width: 10px; cursor: move;"><i class="fa fa-arrows-alt text-muted"></i></div>

                                                    <div class="dropdown col-md-1" style="text-align: right;">
                                                        <a style="text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown" onclick="event.preventDefault();"></a>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="remove" href="javascript:onClickDeletePhone({{ $phone['id'] }});"><b>Delete</b></a></li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="sc2_content" style="display: none;">
                                                <div class="row" style= "display:none">
                                                    <div class="col-md-12" style="margin-top: 10px;">
                                                        <div class="col-md-6" style="font-size: smaller;">NAME</div>
                                                        <div class="col-md-5" style="font-size: smaller;">CONTACT</div>
                                                        <div class="col-md-1"></div>
                                                    </div>
                                                </div>

                                                <a style="margin: 14px;display: inline-block;" href="javascript:onClickAddPhoneDetail({{ $phone['id'] }})" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Phone Detail</a>
                                                <div id="detailDivForPhone-{{ $phone['id'] }}">
                                                @foreach($phone['details'] as $detail)
                                                    <div id="phoneDetailDiv-{{ $detail['id'] }}">
                                                        <div class="row" style="padding-top: 8px; padding-bottom: 8px;">
                                                            <div class="col-md-12 " id="{{ $detail['id'] }}" >
                                                            <div class="col-md-4" > 
                                                                <input type="text" class="col-md-12 sc-input-text"  
                                                                    placeholder="Unit" name="phoneUnit" maxlength="100" value="{{ $detail['unit'] }}" data="{{ $detail['id']  }}" onchange="onChangePhoneUnit(this);">
                                                            </div>
                                                            <div class="col-md-4" >
                                                                <input type="text" class="col-md-12 sc-input-text" 
                                                                    placeholder="Note" name="phoneNote" maxlength="100" value="{{ $detail['note'] }}" data="{{ $detail['id']  }}" onchange="onChangePhoneNote(this);">
                                                            </div>
                                                            <div class="col-md-3" >
                                                                <input type="text" class="col-md-12 sc-input-text" 
                                                                    placeholder="Internal comm." name="phoneInternal" maxlength="100" value="{{ $detail['internal'] }}" data="{{ $detail['id']  }}" onchange="onChangePhoneInternal(this);">
                                                            </div>
                                                            <div class="col-md-1" style="text-align: right;">
                                                                <div class="dropdown">
                                                                    <a style="text-decoration: none; color:#636b6f;padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                                                    <ul class="dropdown-menu">
                                                                        <li><a class="remove" data="{{ $detail['id'] }}" href="javascript:onClickDeletePhoneDetail({{ $detail['id'] }});"><b>Delete</b></a></li>
                                                                    </ul>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>  
                                                @endforeach 
                                                </div>
                                            </div>
                                        </div>  
                                    @endforeach
                                    </div>
                                </div>
                            </div> 
                        @endforeach 
                    </div>   
                </div>              
            </div>
        </div>
    </div>


<!-- Scripts -->
<script type="text/javascript" src="{{ asset('/js/mbt-phones.js') }}?1"></script>
