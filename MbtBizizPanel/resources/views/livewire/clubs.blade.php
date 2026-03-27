<div class="container-fluid" style="margin-top: 25px;">
    <div class="row content">
        @include('leftsidebar')
        <div id = "socialClubsDiv" class="col-md-9" style="margin-top: 15px;">
            <a id="addNewClubLoc" style="margin: 18px;display: inline-block;" href="javascript:onClickAddClubLoc(this);" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Club Location</a>
            <div id="clubLocsDiv">
                @foreach($club_locations as $loc)
                    <div id="locDiv-{{ $loc['id'] }}" >
                        <div class="row sc_collapsible sc_active" style="padding-top: 10px; padding-bottom: 10px; padding-right:0px; margin-left:0px;" >
                            <div class="col-md-12" style="width: calc(100% - 18px); padding-right: 0px; padding-left:0px;">
                                <div class="col-md-11" style="padding-left:0px;">
                                    <input type="text" class="sc-input-text" placeholder="ex: Aksaray" maxlength="100" data="{{ $loc['id'] }}" value="{{ $loc['location'] }}" onchange="onChangeClubLoc(this);">
                                </div>

                                <div class="dropdown col-md-1" style="text-align: right;">
                                    <a style="text-decoration: none; color:#636b6f; padding-left: 18px; padding-right: 18px;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown" onclick="event.preventDefault();"></a>
                                    <ul class="dropdown-menu">
                                        <li><a class="remove" href="javascript:onClickDeleteClubLoc({{ $loc['id'] }});"><b>Delete</b></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="content sc1_content" >
                            <a style="margin: 18px;display: inline-block;" href="addClubEmpty/{{ $loc['id'] }}" ><i class="fa fa-plus-circle" aria-hidden="true"></i> Add New Club</a>
                            <div id="clubsDivForLoc-{{ $loc['id'] }}">
                                
                    
                                    <div class="col-md-12" style="margin-top: 10px;">
                                        <div class="col-md-2 col-md-offset-3">TITLE</div> 
                                        <div class="col-md-3">TEXT</div> 
                                        <div class="col-md-3 ">URL</div> 
                                    </div>
                   

                                <div name="allClubs"  wire:sortable="updateClubOrder" >
                                    @foreach($loc['clubs'] as $club)
                                        <div class="col-md-12 eventdiv passive" style="height: 60px; padding-left: 0px;padding-right: 0px;" 
                                                wire:sortable.item="{{ $club->id }}" wire:key="club-{{ $club->id}}">
                                                
                                                
                                            <div class="col-md-1" wire:sortable.handle style="width: 10px; cursor: move;"><i class="fa fa-arrows-alt text-muted"></i></div>

                                            <div class="col-md-2">
                                                @if ($club->image == null)
                                                    <img src="../img/placeholder_event.jpg" class="col-md-12" style="border-radius: 4px; padding: 0px;height: 40px;">
                                                @else
                                                    <img src="{{ $club->image }}"           class="col-md-12" style="border-radius: 4px; padding: 0px;height: 40px;">
                                                @endif
                                            </div> 
                                            <div class="col-md-2" style="overflow: hidden;text-overflow: ellipsis; height: 40px;line-height: 20px;vertical-align: middle;">{{ $club->title }}</div> 
                                            <div class="col-md-3" style="overflow: hidden;text-overflow: ellipsis; height: 40px;line-height: 20px;vertical-align: middle;">{{ $club->text }}</div> 
                                            <div class="col-md-3" style="overflow: hidden;text-overflow: ellipsis; height: 40px;line-height: 20px;vertical-align: middle;">{{ $club->url }}</div> 

                                            <div class="col-md-1" style="text-align: right; padding-top: 8px; padding-bottom: 13px; padding-right: 0px;">
                                                <a href="editnews-{{ $club->id }}" style="color: inherit; text-decoration: inherit"> </a>
                                                <a style="text-decoration: none; color:#636b6f; width: 100%; width: 100%;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a> 
                                                <ul class="dropdown-menu"> 
                                                    <li><a href="editnews-{{ $club->id }}">Edit</a> </li>
                                                    <li><a class='removeClub' data="{{ $club->id }}"><b>Delete</b></a></li> 
                                                </ul> 
                                            </div> 

                                        </div>
                                    @endforeach
                                </div>
                            </div>
                        </div>
                    </div>  
                @endforeach 
            </div>
        </div>
    </div>
</div>
