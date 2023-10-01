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
            <div class="col-md-9" style="margin-top: 15px;">
                <div class="col-md-9"><h3 style="margin-top: 10px;">News</h3></div>
                <div class="col-md-3">
                    <a href="addnews" type="button" class="col-md-12 btn btn-primary" style="padding-top:10px"
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
                    <div class="col-md-2 col-md-offset-2">LIST TEXT</div>
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
                    {{--<div class="col-md-12 eventdiv passive" id="137">
                        <div class="col-md-2"><img src="img/placeholder_event.jpg"
                                                                             class="col-md-12" style="border-radius: 4px; padding: 0px;"></div>
                        <div class="col-md-3" style="padding-top: 18px; padding-bottom: 18px;">Duyuru Başlığı</div>
                        <div class="col-md-4" style="padding-top: 18px; padding-bottom: 18px;">Duyuru Texti</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Tue, 21.08.2017</div>
                        <div class="col-md-1" style="text-align: right; padding-top: 13px; padding-bottom: 13px;">
                            <div class="dropdown">
                                <a style="text-decoration: none; color:#636b6f;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Edit</a></li>
                                    <li><a class="activate" data="137">Active</a></li>
                                    <li><a class="remove" data="137"><b>Delete</b></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 eventdiv active" id="event2">
                        <div class="col-md-2"><img src="img/placeholder_event.jpg"
                                                   class="col-md-12" style="border-radius: 4px; padding: 0px;"></div>
                        <div class="col-md-3" style="padding-top: 18px; padding-bottom: 18px;">Duyuru Başlığı</div>
                        <div class="col-md-4" style="padding-top: 18px; padding-bottom: 18px;">Duyuru Texti</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Tue, 21.08.2017</div>
                        <div class="col-md-1" style="text-align: right; padding-top: 13px; padding-bottom: 13px;">
                            <div class="dropdown">
                                <a style="text-decoration: none; color:#636b6f;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Edit</a></li>
                                    <li><a data-toggle="modal" data-target="#event_translations">Send Notification</a></li>
                                    <li><a class="activate" data="137">Passive</a></li>
                                    <li><a class="remove" data="137"><b>Delete</b></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>--}}
                    {{--<div class="col-md-12 eventdiv active" id="event3">
                        <div class="col-md-2"><img src="img/placeholder_event.jpg"
                                                   class="col-md-12" style="border-radius: 4px; padding: 0px;"></div>
                        <div class="col-md-3" style="padding-top: 18px; padding-bottom: 18px;">Etkinlik Başlığı</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Tue, 21.08.2017</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">10:00 - 18:00</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Ulker Sports Arena</div>
                        <div class="col-md-1" style="text-align: right; padding-top: 13px; padding-bottom: 13px;">
                            <div class="dropdown">
                                <a style="text-decoration: none; color:#636b6f;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Düzenle</a></li>
                                    <li><a data-toggle="modal" data-target="#event_translations">Çeviriler</a></li>
                                    <li><a href="#">Pasif Yap</a></li>
                                    <li><a class="remove" data="event3"><b>Sil</b></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 eventdiv active" id="event4">
                        <div class="col-md-2"><img src="img/placeholder_event.jpg"
                                                   class="col-md-12" style="border-radius: 4px; padding: 0px;"></div>
                        <div class="col-md-3" style="padding-top: 18px; padding-bottom: 18px;">Etkinlik Başlığı</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Tue, 21.08.2017</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">10:00 - 18:00</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Ulker Sports Arena</div>
                        <div class="col-md-1" style="text-align: right; padding-top: 13px; padding-bottom: 13px;">
                            <div class="dropdown">
                                <a style="text-decoration: none; color:#636b6f;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Düzenle</a></li>
                                    <li><a data-toggle="modal" data-target="#event_translations">Çeviriler</a></li>
                                    <li><a href="#">Pasif Yap</a></li>
                                    <li><a class="remove" data="event4"><b>Sil</b></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 eventdiv active" id="event5">
                        <div class="col-md-2"><img src="img/placeholder_event.jpg"
                                                   class="col-md-12" style="border-radius: 4px; padding: 0px;"></div>
                        <div class="col-md-3" style="padding-top: 18px; padding-bottom: 18px;">Etkinlik Başlığı</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Tue, 21.08.2017</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">10:00 - 18:00</div>
                        <div class="col-md-2" style="padding-top: 18px; padding-bottom: 18px;">Ulker Sports Arena</div>
                        <div class="col-md-1" style="text-align: right; padding-top: 13px; padding-bottom: 13px;">
                            <div class="dropdown">
                                <a style="text-decoration: none; color:#636b6f;" class="fa fa-ellipsis-v fa-2x dropdown-toggle" aria-hidden="true" type="button" data-toggle="dropdown"></a>
                                <ul class="dropdown-menu">
                                    <li><a href="#">Düzenle</a></li>
                                    <li><a data-toggle="modal" data-target="#event_translations">Çeviriler</a></li>
                                    <li><a href="#">Pasif Yap</a></li>
                                    <li><a class="remove" data="event5"><b>Sil</b></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>--}}
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
