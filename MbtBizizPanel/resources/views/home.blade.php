@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar') {{-- sol menu --}}

            <div class="col-md-9" style="margin-top: 15px;">
                <div class="col-md-9"><h3 style="margin-top: 10px;">News</h3></div>
                <div class="col-md-3">
                    <a href="addnewsEmpty" type="button" class="col-md-12 btn btn-primary" style="padding-top:10px"
                       name="addNews"><i class="fa fa-plus" aria-hidden="true"></i> Add News</a>
                </div>
                <div class="col-md-5" style="padding-right: 0px;">
                    <div class="form-group">
                        <div class="icon-addon addon-md">
                            <input type="text" placeholder="Search" class="form-control" id="arama" name="searchNews">
                            <label for="arama" class="fa fa-search" rel="tooltip" title="Arama"
                                   style="padding-top: 15px;"></label>
                        </div>
                    </div>
                </div>
                <br>
                <div class="col-md-12" style="margin-top: 10px;">
                    <div class="col-md-2 col-md-offset-2">TITLE</div>
                    <div class="col-md-1">TYPE</div>
                    <div class="col-md-2">DISCOUNT TYPE</div>
                    <div class="col-md-2" style="cursor: pointer" id="sortByDate">DATE (on mobile)
                        <i class="fa fa-caret-down" aria-hidden="true" style="margin-left: 10px;"></i>
                    </div>
                    <div class="col-md-2" style="cursor: pointer" id="sortByCreatedAt">CREATED AT
                        <i class="fa fa-caret-down" aria-hidden="true" style="margin-left: 10px;"></i>
                    </div>
                    <input type="hidden" name="sortBy" id="sortBy" value="1">
                </div>
                <div id="allNews">
                </div>
            </div>
        </div>
        <div id="sendnotif" class="modal fade in" role="dialog">
            <div class="modal-dialog modal-md">
                <form id="sendnotification" action="javascript: void(0)">
                    <div class="modal-content">
                        <div class="modal-header" style="border-bottom: 0px;">
                            <h4 class="modal-title col-md-8">Send Notification To: <br>
                            </h4>
                            <div class="col-md-4">
                                <a class="col-md-12" data-dismiss="modal" style="padding-top: 12px; text-align: right">Kapat</a>
                            </div>
                        </div>
                        <div class="modal-body"
                             style="display: inline-block; overflow-y: auto; max-height: 600px; width: 100%;">
                            <div class="col-md-12" id="notifType">
                            </div>
                            <div class="col-md-offset-8 col-md-4">
                                <a type="button" name="sendcollar" class="col-md-12 btn btn-primary collartype"
                                   style="padding-top: 10px;" data-dismiss="modal">Send</a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
@endsection
