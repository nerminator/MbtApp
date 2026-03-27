
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
            <div id = "socialMediaDiv" class="col-md-9" style="margin-top: 15px;">
                <a id="addNewMedia" style="margin: 18px;display: inline-block;" href="javascript:onClickAddMedia(this);" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Platform</a>
                <div id="mediasDiv" >
                    @foreach($medias as $media)
                        <div id="mediaDiv-{{ $media['id'] }}" wire:sortable.item="{{ $media['id']}}" wire:key="media-{{ $media['id']}}">
                            <div class="row sc_collapsible sc_active" style="padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;" >
                                <div class="col-md-12" style="width: calc(100% - 18px); padding-right: 0px; padding-left:0px;">
                                    <div class="col-md-11" style="padding-left:0px;">
                                        <input type="text" class="sc-input-text" placeholder="ex: Instagram" maxlength="100" data="{{ $media['id'] }}" value="{{ $media['name'] }}" onchange="onChangeMedia(this);">
                                    </div>

                                    <div class="dropdown col-md-1" style="text-align: right;">
                                        <a style="text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown" onclick="event.preventDefault();"></a>
                                        <ul class="dropdown-menu">
                                            <li><a class="remove" href="javascript:onClickDeleteMedia({{ $media['id'] }});"><b>Delete</b></a></li>
                                        </ul>
                                    </div>
                                </div>
                            </div>

                            <div class="content sc1_content" style="display: block;">
                                <a style="margin: 18px;display: inline-block;" href="javascript:onClickAddMediaDetail({{ $media['id'] }});" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Media Detail</a>
                                <div id="detailDivForMedia-{{ $media['id'] }}" wire:sortable="updateMediaDetailOrder">
                                    @foreach($media['details'] as $detail)
                                        <div id="detailDiv-{{ $detail['id'] }}" wire:sortable.item="{{ $detail['id']}}" wire:key="detail-{{ $detail['id']}}">
                                            <div class="row" style="padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;" >
                                                <div class="col-md-12" style="width: calc(100% - 18px); padding-right: 0px; padding-left:0px;">
                                                    <div class="col-md-4" style="padding-left:0px;">
                                                        <input type="text" class="sc-input-text" placeholder="ex: Mercedes-Benz Türk" maxlength="100" data="{{ $detail['id'] }}" value="{{ $detail['account'] }}" onchange="onChangeMediaAccount(this);">
                                                    </div>
                                                    <div class="col-md-6" >
                                                        <input type="text" class="sc-input-text" placeholder="ex: https://www.instagram.com/mercedesbenzturk/" maxlength="100" value="{{ $detail['url'] }}" data="{{ $detail['id']  }}" onchange="onChangeMediaUrl(this);">
                                                    </div>

                                                    <div class="col-md-1" wire:sortable.handle style="width: 10px; cursor: move;"><i class="fa fa-arrows-alt text-muted"></i></div>

                                                    <div class="dropdown col-md-1" style="text-align: right;">
                                                        <a style="text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown" onclick="event.preventDefault();"></a>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="remove" href="javascript:onClickDeleteMediaDetail({{ $detail['id'] }});"><b>Delete</b></a></li>
                                                        </ul>
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
    </div>

