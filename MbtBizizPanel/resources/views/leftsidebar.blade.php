<div class="col-md-3 sidenav" style="padding: 0px; padding-left:50px;">
    <h4 style="text-align: left">
        <img src="{{ asset('img/logo4.png') }}" style="margin-left: -42px;">
    </h4>

    <ul class="nav nav-pills nav-stacked" style="margin-top:20px;">
        <li class="{{ request()->is('dashboard') ? 'active' : '' }}">
            <a href="{{ url('dashboard') }}" style="padding-left: 0px;">Dashboard</a>
        </li>

        <li class="{{ request()->is('home') ? 'active' : '' }}">
            <a href="{{ url('home') }}" style="padding-left: 0px;">News</a>
        </li>

        <li class="{{ request()->is('socialclubs') ? 'active' : '' }}">
            <a href="{{ url('socialclubs') }}" style="padding-left: 0px;">Social Clubs</a>
        </li>

        <li class="{{ request()->is('phones') ? 'active' : '' }}">
            <a href="{{ url('phones') }}" style="padding-left: 0px;">Phone Numbers</a>
        </li>

        <li class="{{ request()->is('medias') ? 'active' : '' }}">
            <a href="{{ url('medias') }}" style="padding-left: 0px;">Social Media</a>
        </li>

        <li class="{{ request()->is('feedback') ? 'active' : '' }}">
            <a href="{{ url('feedback') }}" style="padding-left: 0px;">App Feedback</a>
        </li>

        <li class="{{ request()->is('digitalcard') ? 'active' : '' }}">
            <a href="{{ url('digitalcard') }}" style="padding-left: 0px;">Digital Card</a>
        </li>

        <li class="{{ request()->is('payslip') ? 'active' : '' }}">
            <a href="{{ url('payslip') }}" style="padding-left: 0px;">Payslip</a>
        </li>

        <li class="{{ request()->is('aboutus') ? 'active' : '' }}">
            <a href="{{ url('aboutus') }}" style="padding-left: 0px;">About Us</a>
        </li>

        <li class="{{ request()->is('about') ? 'active' : '' }}">
            <a href="{{ url('about') }}" style="padding-left: 0px;">About</a>
        </li>

        <li class="{{ request()->is('appDescription') ? 'active' : '' }}">
            <a href="{{ url('appDescription') }}" style="padding-left: 0px;">App Description</a>
        </li>
    </ul>

    <br>

    <div class="logout">
        <img src="{{ asset('img/avatar@3x.png') }}" style="border-radius: 100%; width: 30px;">
        <label>{{ Auth::user()->name }}</label> <br>

        <a style="padding-left: 35px;" href="{{ route('logout') }}"
           onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
            <i class="fa fa-sign-out" aria-hidden="true"></i> Logout
        </a>

        <form id="logout-form" action="{{ route('logout') }}" method="GET" style="display: none;">
            {{ csrf_field() }}
        </form>
    </div>
</div>