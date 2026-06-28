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
                .dashboard-filter-tabs {
                    display: inline-flex;
                    gap: 6px;
                    flex-wrap: wrap;
                    margin: 16px 0 18px;
                }
                .dashboard-filter-tab {
                    display: inline-flex;
                    align-items: center;
                    justify-content: center;
                    padding: 10px 16px;
                    border: 1px solid #2f3b52;
                    border-radius: 10px;
                    color: #9fb3d1;
                    background: #111827;
                    text-decoration: none;
                    font-weight: 600;
                    transition: background-color .15s ease, color .15s ease;
                }
                .dashboard-filter-tab:hover,
                .dashboard-filter-tab:focus {
                    color: #ffffff;
                    text-decoration: none;
                    background: #162033;
                }
                .dashboard-filter-tab.active {
                    color: #ffffff;
                    background: linear-gradient(180deg, #1d4ed8 0%, #1e40af 100%);
                    box-shadow: inset 0 -2px 0 rgba(255,255,255,0.12);
                }
                .dashboard-filter-form {
                    display: flex;
                    gap: 12px;
                    flex-wrap: wrap;
                    align-items: end;
                    margin: 0 0 14px;
                    padding: 16px;
                    border: 1px solid #243041;
                    border-radius: 12px;
                    background: #0b1220;
                    max-width: 900px;
                }
                .dashboard-filter-form label {
                    color: #c5d1e0;
                    display: block;
                    margin-bottom: 4px;
                    font-size: 12px;
                    text-transform: uppercase;
                    letter-spacing: .04em;
                }
                .dashboard-filter-form input {
                    background: #0f172a;
                    border: 1px solid #334155;
                    color: #ffffff;
                }
                .dashboard-filter-form .btn {
                    min-width: 110px;
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
            @php
                $customStartDate = $activeUsersFilter['start_date'] ?? now()->subDays(30)->toDateString();
                $customEndDate = $activeUsersFilter['end_date'] ?? now()->toDateString();
                $selectedUserType = $activeUsersTypeFilter['value'];

                $baseChartParams = ['user_type' => $selectedUserType];
                $customChartParams = [
                    'period' => 'custom',
                    'start_date' => $customStartDate,
                    'end_date' => $customEndDate,
                    'user_type' => $selectedUserType,
                ];

                $typeParams = [
                    'all' => [
                        'period' => $activeUsersFilter['period'],
                        'start_date' => $activeUsersFilter['period'] === 'custom' ? $customStartDate : null,
                        'end_date' => $activeUsersFilter['period'] === 'custom' ? $customEndDate : null,
                        'user_type' => 'all',
                    ],
                    'white' => [
                        'period' => $activeUsersFilter['period'],
                        'start_date' => $activeUsersFilter['period'] === 'custom' ? $customStartDate : null,
                        'end_date' => $activeUsersFilter['period'] === 'custom' ? $customEndDate : null,
                        'user_type' => 'white',
                    ],
                    'blue' => [
                        'period' => $activeUsersFilter['period'],
                        'start_date' => $activeUsersFilter['period'] === 'custom' ? $customStartDate : null,
                        'end_date' => $activeUsersFilter['period'] === 'custom' ? $customEndDate : null,
                        'user_type' => 'blue',
                    ],
                    'other' => [
                        'period' => $activeUsersFilter['period'],
                        'start_date' => $activeUsersFilter['period'] === 'custom' ? $customStartDate : null,
                        'end_date' => $activeUsersFilter['period'] === 'custom' ? $customEndDate : null,
                        'user_type' => 'other',
                    ],
                ];
            @endphp

            <div class="card mt-4 p-3" style="min-height: 520px;">
                <h3>{{ $activeUsersFilter['title'] }}</h3>

                <div class="dashboard-filter-tabs">
                    <a href="{{ route('dashboard', array_merge($baseChartParams, ['period' => '30d'])) }}"
                       class="dashboard-filter-tab {{ $activeUsersFilter['period'] === '30d' ? 'active' : '' }}">
                        Last 30 Days
                    </a>
                    <a href="{{ route('dashboard', array_merge($baseChartParams, ['period' => '3m'])) }}"
                       class="dashboard-filter-tab {{ $activeUsersFilter['period'] === '3m' ? 'active' : '' }}">
                        Last 3 Months
                    </a>
                    <a href="{{ route('dashboard', array_merge($baseChartParams, ['period' => '1y'])) }}"
                       class="dashboard-filter-tab {{ $activeUsersFilter['period'] === '1y' ? 'active' : '' }}">
                        Last 1 Year
                    </a>
                    <a href="{{ route('dashboard', $customChartParams) }}"
                       class="dashboard-filter-tab {{ $activeUsersFilter['period'] === 'custom' ? 'active' : '' }}">
                        Custom Interval
                    </a>
                </div>

                <div class="dashboard-filter-tabs" style="margin-top: -4px; margin-bottom: 18px;">
                    <a href="{{ route('dashboard', array_filter($typeParams['all'], fn($value) => !is_null($value))) }}"
                       class="dashboard-filter-tab {{ $selectedUserType === 'all' ? 'active' : '' }}">
                        All
                    </a>
                    <a href="{{ route('dashboard', array_filter($typeParams['white'], fn($value) => !is_null($value))) }}"
                       class="dashboard-filter-tab {{ $selectedUserType === 'white' ? 'active' : '' }}">
                        White Collar
                    </a>
                    <a href="{{ route('dashboard', array_filter($typeParams['blue'], fn($value) => !is_null($value))) }}"
                       class="dashboard-filter-tab {{ $selectedUserType === 'blue' ? 'active' : '' }}">
                        Blue Collar
                    </a>
                    <a href="{{ route('dashboard', array_filter($typeParams['other'], fn($value) => !is_null($value))) }}"
                       class="dashboard-filter-tab {{ $selectedUserType === 'other' ? 'active' : '' }}">
                        Others
                    </a>
                </div>

                @if ($activeUsersFilter['period'] === 'custom')
                    <form method="GET" action="{{ route('dashboard') }}" class="dashboard-filter-form">
                        <input type="hidden" name="period" value="custom">
                        <input type="hidden" name="user_type" value="{{ $selectedUserType }}">
                        <div>
                            <label for="active-users-start-date">Start Date</label>
                            <input
                                id="active-users-start-date"
                                type="date"
                                name="start_date"
                                class="form-control"
                                value="{{ $customStartDate }}"
                                required
                            >
                        </div>
                        <div>
                            <label for="active-users-end-date">End Date</label>
                            <input
                                id="active-users-end-date"
                                type="date"
                                name="end_date"
                                class="form-control"
                                value="{{ $customEndDate }}"
                                required
                            >
                        </div>
                        <div>
                            <button type="submit" class="btn btn-primary">Apply</button>
                        </div>
                    </form>
                @endif

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
                            <th>Total Views</th>
                            <th>White Collar Views</th>
                            <th>Blue Collar Views</th>
                            <th>Other Views</th>
                        </tr>
                    </thead>
                    <tbody>
                        @forelse ($newsViews as $item)
                            <tr>
                                <td>
                                    <a href="{{ url('/editnews-' . $item['news_id']) }}"
                                       style="color:#58a6ff; text-decoration: underline;">
                                        {{ $item['title'] }}
                                    </a>
                                </td>
                                <td>{{ number_format($item['views']) }}</td>
                                <td>{{ number_format($item['white']) }}</td>
                                <td>{{ number_format($item['blue']) }}</td>
                                <td>{{ number_format($item['other']) }}</td>
                            </tr>
                        @empty
                            <tr>
                                <td colspan="5" class="text-muted">No announcement view data found.</td>
                            </tr>
                        @endforelse
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

    const canvas = document.getElementById('activeUsersChart');

    if (!canvas) {
        return;
    }

    const ctx = canvas.getContext('2d');

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Active Users',
                data: data,
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