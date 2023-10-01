<!DOCTYPE html>
<html lang="{{ app()->getLocale() }}">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- CSRF Token -->
    <meta name="csrf-token" content="{{ csrf_token() }}">

    <title>{{ config('app.name', 'Laravel') }}</title>

    <!-- Styles -->
    <link href="{{ asset('/css/app.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/mbt-biziz.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/font-awesome.min.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/jquery.simplewizard.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/jquery-ui.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/jquery.loader.min.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/jquery.loader.min.css') }}" rel="stylesheet">
    <link href="{{ asset('/css/bootstrap-select.min.css') }}" rel="stylesheet">
    <link rel="shortcut icon" href="{{ asset('favicon.ico') }}">

    <style>
        #password {
            -webkit-text-security:disc;
        }
        #current_password
        {
            -webkit-text-security:disc;
        }
        #password-confirm {
            -webkit-text-security:disc;
        }
        .center-block {
            float: none;
            margin-left: auto;
            margin-right: auto;
        }

        .input-group .icon-addon .form-control {
            border-radius: 0;
        }

        .icon-addon {
            position: relative;
            color: #555;
            display: block;
        }

        .icon-addon:after,
        .icon-addon:before {
            display: table;
            content: " ";
        }

        .icon-addon:after {
            clear: both;
        }

        .icon-addon.addon-md .glyphicon,
        .icon-addon .glyphicon,
        .icon-addon.addon-md .fa,
        .icon-addon .fa {
            position: absolute;
            z-index: 2;
            left: 10px;
            font-size: 14px;
            width: 20px;
            margin-left: -2.5px;
            text-align: center;
            padding: 10px 0;
            top: 1px
        }

        .icon-addon.addon-lg .form-control {
            line-height: 1.33;
            height: 46px;
            font-size: 18px;
            padding: 10px 16px 10px 40px;
        }

        .icon-addon.addon-sm .form-control {
            height: 30px;
            padding: 5px 10px 5px 28px;
            font-size: 12px;
            line-height: 1.5;
        }

        .icon-addon.addon-lg .fa,
        .icon-addon.addon-lg .glyphicon {
            font-size: 18px;
            margin-left: 0;
            left: 11px;
            top: 4px;
        }

        .icon-addon.addon-md .form-control,
        .icon-addon .form-control {
            padding-left: 30px;
            float: left;
            font-weight: normal;
        }

        .icon-addon.addon-sm .fa,
        .icon-addon.addon-sm .glyphicon {
            margin-left: 0;
            font-size: 12px;
            left: 5px;
            top: -1px
        }

        .icon-addon .form-control:focus + .glyphicon,
        .icon-addon:hover .glyphicon,
        .icon-addon .form-control:focus + .fa,
        .icon-addon:hover .fa {
            color: #2580db;
        }
    </style>
</head>
<body>
<div id="app">
    @yield('content')
</div>

<!-- Scripts -->
<script type="text/javascript"
        src="//maps.googleapis.com/maps/api/js?key=AIzaSyCrMlOqT-AuG8n_8_2oduW1XRw-OIs-DCU&libraries=places"></script>
<script src="{{ asset('/js/app.js') }}"></script>
<script src="{{ asset('/js/jquery-ui.js') }}"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.14.0/jquery.validate.min.js"></script>
<script src="{{ asset('/js/jquery.simplewizard.js') }}"></script>
<script src="{{ asset('/js/jquery.cropit.js') }}"></script>
<script src="{{ asset('js/moment.js') }}"></script>
<script src="{{ asset('/js/mbt-biziz.js') }}"></script>
<script src="{{ asset('js/jquery.visible.js') }}"></script>
<script src="{{ asset('js/jquery.loader.min.js') }}"></script>
<script src="{{ asset('js/bootstrap-select.min.js') }}"></script>
<script src='https://www.google.com/recaptcha/api.js'></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>
