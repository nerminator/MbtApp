@extends('layouts.app')

@section('content')
    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
            <div id = "dashboardDiv" class="col-md-9" style="margin-top: 15px;">
            <h2>Dashboard</h2>


            <!-- KPI BOXES WITH ICONS + ANIMATION -->
            <style>
                .kpi-card {
                    background-color:#111;
                    color:white;
                    border:1px solid #222;
                    transition: transform .2s ease-in-out;
                }
                .kpi-card:hover {
                    transform: translateY(-4px);
                    box-shadow: 0 0 12px rgba(0, 255, 255, 0.35);
                }
                .kpi-icon {
                    font-size: 26px;
                    opacity:.75;
                    margin-bottom: 8px;
                }
                /* Ensure font awesome icons display correctly */
                .fa, .fa-solid, .fas, .fa-regular, .far {
                    font-family: "Font Awesome 6 Free" !important;
                    font-weight: 900 !important;
                }
            </style>

            <div class="row mb-4 text-center">

                <p class="text-muted fst-italic mt-1" style="margin-left:14px; text-align:left; font-size: 1.2rem; opacity: 0.7;">
                    Note: These numbers are total logged in users after 17 Sept 2025
                </p>

                <!-- ✅ First KPI (new: "Total out of") -->
                <div class="col-md-3 mb-3">
                    <div class="card kpi-card shadow-sm h-100">
                        <div class="card-body d-flex flex-column justify-content-center">
                            <i class="fa-solid fa-users kpi-icon"></i>
                            <h2 class="fw-bold counter" data-target="{{ $totalLoggedIn }}">0</h2>
                            <small class="text-info">Users out of {{ number_format($totalUsers) }}</small>
                        </div>
                    </div>
                </div>

                <!-- WHITE COLLAR -->
                <div class="col-md-3 mb-3">
                    <div class="card kpi-card shadow-sm h-100">
                        <div class="card-body d-flex flex-column justify-content-center">
                            <i class="fa-solid fa-user-tie kpi-icon"></i>
                            <h2 class="fw-bold counter" data-target="{{ $whiteLoggedIn }}">0</h2>
                            <small class="text-info">White collar users out of {{ number_format($totalWhiteUsers) }}</small>
                        </div>
                    </div>
                </div>

                <!-- BLUE COLLAR -->
                <div class="col-md-3 mb-3">
                    <div class="card kpi-card shadow-sm h-100">
                        <div class="card-body d-flex flex-column justify-content-center">
                            <i class="fa-solid fa-hard-hat kpi-icon"></i>
                            <h2 class="fw-bold counter" data-target="{{ $blueLoggedIn }}">0</h2>
                            <small class="text-info">Blue collar users out of {{ number_format($totalBlueUsers) }}</small>
                        </div>
                    </div>
                </div>

                <!-- OTHER (NULL TYPE) -->
                <div class="col-md-3 mb-3">
                    <div class="card kpi-card shadow-sm h-100">
                        <div class="card-body d-flex flex-column justify-content-center">
                            <i class="fa-solid fa-question-circle kpi-icon"></i>
                            <h2 class="fw-bold counter" data-target="{{ $otherLoggedIn }}">0</h2>
                            <small class="text-info">Other users out of {{ number_format($totalOtherUsers) }}</small>
                        </div>
                    </div>
                </div>


            </div>

            <!-- Active Users Chart -->
            <div class="card mt-4 p-3" style="height: 350px;">
                <h3>Active Users (Last 30 Days)</h3>

                <div style="position: relative; height:300px; width:100%; max-width: 900px;">
                    <canvas id="activeUsersChart"></canvas>
                </div>
            </div>

            <!-- Most viewed news -->
            <div class="card mt-4 p-3">
                <h3>Most Viewed Announcements</h3>

                <table class="table" style="color: gray;">
                    <thead>
                        <tr>
                            <th>News Title</th>
                            <th>Views</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach ($newsViews as $item)
                        <tr>
                            <td>
                                <a href="{{ url('/editnews-' . $item['news_id']) }}"
                                   style="color:#58a6ff; text-decoration: underline;">
                                    {{ $item['title'] }}
                                </a>
                            </td>
                            <td>{{ $item['views'] }}</td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            </div>

            <!-- Most viewed categories (menus) -->
            <div class="card mt-4 p-3">
                <h3>Most Viewed Categories</h3>

                <table class="table" style="color: gray;">
                    <thead>
                    <tr>
                        <th>Category</th>
                        <th>Total Views</th>
                        <th>WhiteCollar Views</th>
                        <th>Blue Collar Views</th>
                        <th>Other Views</th>
                    </tr>
                    </thead>
                    <tbody>
                    @php
                        $menuLabels = [
                            'about_us' => 'About Us',
                            'campaigns' => 'Campaigns',
                            'announcements' => 'Announcements',
                            // ...
                        ];
                    @endphp

                    @forelse ($menuViews as $item)
                        @php
                            $key = $item['menu_key'];
                            $label = $menuLabels[$key] ?? ucwords(str_replace(['_', '-'], ' ', $key));
                        @endphp
                        <tr>
                            <td>{{ $label }}</td>
                            <td>{{ number_format($item['total']) }}</td>
                            <td>{{ number_format($item['white']) }}</td>
                            <td>{{ number_format($item['blue']) }}</td>
                            <td>{{ number_format($item['other']) }}</td>
                        </tr>
                    @empty
                        <tr>
                            <td colspan="5" class="text-muted">No category view data found.</td>
                        </tr>
                    @endforelse
                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>
@endsection


@section('scripts')
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
document.addEventListener("DOMContentLoaded", function () {

    const labels = @json($activeUsersChart['labels']);
    const data = @json($activeUsersChart['data']);

    console.log("Chart received:", labels, data); // <-- debug

    const canvas = document.getElementById('activeUsersChart');

    if (!canvas) {
        console.error("Canvas #activeUsersChart not found!");
        return;
    }

    const ctx = canvas.getContext('2d');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Active Users',
                data: @json($activeUsersChart['data']),
                borderWidth: 2,
                fill: true,
                borderColor: '#00AEEF',
                backgroundColor: 'rgba(0, 174, 239, 0.35)',
                pointBackgroundColor: '#00AEEF',
                tension: 0.3,
            }]
        },
        options: {
            maintainAspectRatio: false,   // <-- IMPORTANT FIX
            responsive: true,
            plugins: {
                legend: {
                    labels: {
                        color: "white"
                    }
                }
            },
            scales: {
                x: {
                    ticks: { color: 'white' },
                    grid: { color: 'rgba(255,255,255,0.1)' }
                },
                y: {
                    ticks: { color: 'white' },
                    beginAtZero: true,
                    grid: { color: 'rgba(255,255,255,0.1)' }
                }
            }
        }
    });
});
</script>

<script>
const counters = document.querySelectorAll('.counter');
counters.forEach(counter => {
    const animate = () => {
        const target = +counter.getAttribute('data-target');
        const current = +counter.innerText.replace(/,/g, '');
        const increment = target / 70;

        if (current < target) {
            counter.innerText = Math.ceil(current + increment).toLocaleString('en-US');
            requestAnimationFrame(animate);
        } else {
            counter.innerText = target.toLocaleString('en-US');
        }
    };
    animate();
});
</script>

@endsection