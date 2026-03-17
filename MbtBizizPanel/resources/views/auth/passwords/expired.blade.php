@extends('layouts.app')

@section('content')
    <div class="container" style="margin-top:150px;">
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <div class="panel panel-default" style="background-color: #000000; border-color: #000000">
                    <div class="panel-heading" style="background-color: #000000; border-color: #000000; color:#ffffff; text-align: center;">Şifreyi Sıfırla</div>
                    <div class="panel-body">
                        @if (session('status'))
                            <div class="alert alert-success">
                                {{ session('status') }}
                            </div>
                            <a href="/bizizPanel/public/index.php/home">Anasayfaya geri dön.</a>
                        @else
                            <div class="alert alert-info">
                                Şifrenizin süresi doldu, lütfen değiştirin.
                            </div>
                            <form class="form-horizontal" method="POST" action="{{ route('password.post_expired') }}">
                                {{ csrf_field() }}

                                <div class="form-group{{ $errors->has('current_password') ? ' has-error' : '' }}">
                                    <label for="current_password" class="col-md-4 control-label">Mevcut Şifre</label>

                                    <div class="col-md-6">
                                        <input id="current_password" type="text" class="form-control" name="current_password" required="">

                                        @if ($errors->has('current_password'))
                                            <span class="help-block">
                                        <strong>{{ $errors->first('current_password') }}</strong>
                                    </span>
                                        @endif
                                    </div>
                                </div>

                                <div class="form-group{{ $errors->has('password') ? ' has-error' : '' }}">
                                    <label for="password" class="col-md-4 control-label">Yeni Şifre</label>

                                    <div class="col-md-6">
                                        <input id="password" type="text" class="form-control" name="password" required="">

                                        @if ($errors->has('password'))
                                            <span class="help-block">
                                        <strong>{{ $errors->first('password') }}</strong>
                                    </span>
                                        @endif
                                    </div>
                                </div>

                                <div class="form-group{{ $errors->has('password_confirmation') ? ' has-error' : '' }}">
                                    <label for="password-confirm" class="col-md-4 control-label">Yeni Şifre (Tekrar)</label>
                                    <div class="col-md-6">
                                        <input id="password-confirm" type="text" class="form-control" name="password_confirmation" required="">

                                        @if ($errors->has('password_confirmation'))
                                            <span class="help-block">
                                        <strong>{{ $errors->first('password_confirmation') }}</strong>
                                    </span>
                                        @endif
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-md-6 col-md-offset-4">
                                        <button type="submit" class="btn btn-primary">
                                            Reset Password
                                        </button>
                                    </div>
                                </div>
                            </form>
                        @endif
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection