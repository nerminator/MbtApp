<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Digital Business Card</title>

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            background: #0D0D0D;
            font-family: Arial, sans-serif;
            color: white;
            margin: 0;
            padding: 0;
            text-align: center;
        }

        .card-container {
            max-width: 480px;
            margin: auto;
            background: #202020;
            border-radius: 18px;
            box-shadow: 0 0 25px rgba(0, 0, 0, .5);
            padding-top : 18px;
            padding-left : 10px;
            padding-right : 10px;
            padding-bottom : 18px;
        }

        .header-logo {
            width: 60px;
            margin: 18px;
        }

        .info-row {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 20px;
            margin-top: 20px;
            margin-bottom: 20px;
        }

        .qr-image {
            width: 130px;
            cursor: pointer;
            transition: transform .25s ease;
            border-radius: 0px;
        }

        /* Fullscreen modal that captures all gestures */
        .qr-modal {
          position: fixed; inset: 0;
          background: rgba(0,0,0,0.85);
          display: none;
          align-items: center;
          justify-content: center;
          z-index: 99999;
          overscroll-behavior: contain;
        }

        .qr-modal.show {
          display: flex;
        }

        .qr-modal img {
          width: 70%;
          border-radius: 0px;
          box-shadow: 0 8px 40px rgba(0,0,0,.6);
          touch-action: none;
        }

        .user-info {
            text-align: left;
            max-width: 260px;
        }

        .user-name {
            font-size: 20px;
            margin: 0 0 6px 0;
        }

        .user-title {
            font-size: 15px;
            color: #C8C8C8;
            margin: 0 0 6px 0;
        }

        .company-name {
            font-size: 15px;
            color: #C8C8C8;
            margin: 0;
        }

        .user-location {
            font-size: 15px;
            color: #C8C8C8;
            margin: 0;
        }

        .contact-info {
            display: flex;
            flex-direction: column;
            margin-bottom: 8px;
            gap: 4px;
        }

      .contact-link {
          color: #4aa3ff;
          text-decoration: none;
          font-size: 12px;
          font-weight: 500;
      }

      .contact-link:hover {
          text-decoration: underline;
          color: #6bb8ff;
      }

        .contact-icon {
            color: #c7c7c7;  /* açık gri */
            font-size: 12px;
            margin-right: 2px;
        }

        /* LinkedIn small button */
        .links-row {
            margin-top: 10px;
            margin-bottom: 5px;
        }

        .linkedin-btn {
            background-color: #0077b5;
            padding: 6px 16px;
            border-radius: 6px;
            font-size: 15px;
            color: white;
            display: inline-flex;
            align-items: center;
            gap: 6px;
            text-decoration: none;
            transition: 0.25s;
        }
        .linkedin-btn:hover {
            background-color: #005f8d;
        }

        /* Video styling */
        .video-wrapper {
            width: 100%;
            margin: 0 auto;
            border-radius: 0px;
            overflow: hidden;
        }
        .video-wrapper iframe {
            width: 100%;
            height: 240px;
            border: none !important;
            outline: none !important;
            box-shadow: none !important;
        }

        /* Add To Contacts CTA */
        .add-contact-btn {
            background-color: #1074E7;
            text-align: center;
            padding: 14px;
            border-radius: 10px;
            font-size: 17px;
            color: white;
            margin: 20px auto 5px auto;
            display: block;
            font-weight: bold;
            text-decoration: none;
        }
        .add-contact-btn:hover {
            background-color: #0a56ad;
        }
    </style>
</head>
<body>

<!-- Mercedes Logo -->
<img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Mercedes-Logo.svg/567px-Mercedes-Logo.svg.png?20230111203159" class="header-logo">

<div class="card-container">

    <h1 class="user-name">{{ strtoupper($user->name_surname) }}</h1>

    @if ($user->title_businesscard)
        <p class="user-title">{{ $user->title_businesscard }}</p>
    @elseif ($user->title)
        <p class="user-title">{{ $user->title }}</p>
    @endif


    <!-- QR + User Info -->
    <div class="info-row">

        <img class="qr-image" id="qrCode"
             src="https://api.qrserver.com/v1/create-qr-code/?size=200x200&data={{ urlencode(url('/digitalCard/' . $user->dk_uuid)) }}"
             alt="QR Code"
             onclick="toggleQrFullscreen()">

        <div class="user-info">

            <div class="contact-info">
                @if($user->mobile_phone)
                    <a href="tel:{{ $user->mobile_phone }}" class="contact-link">
                        <i class="fa-solid fa-phone contact-icon"></i>
                        {{ $user->mobile_phone }}
                    </a>
                @endif

                @if($user->email)
                    <a href="mailto:{{ $user->email }}" class="contact-link email-text">
                        <i class="fa-solid fa-envelope contact-icon"></i>
                        {{ $user->email }}
                    </a>
                @endif
            </div>

            <p class="company-name">Mercedes-Benz Türk A.Ş.</p>
            <p class="user-location">{{ $user->location }}</p>

                <!-- LinkedIn Button -->
              @if (!empty($linkedinUrl))
                  <div class="links-row">
                      <a href="{{ $linkedinUrl }}" target="_blank" class="linkedin-btn">
                          <i class="fab fa-linkedin"></i> LinkedIn
                      </a>
                  </div>
              @endif
        </div>
    </div>




    <!-- Video -->
    @if (!empty($embedUrl))
        <div class="video-wrapper">
            <iframe
                width="100%"
                height="220"
                src="{{ $embedUrl }}"
                allow="autoplay; encrypted-media; fullscreen"
                style="border: none;"
                playsinline
                allowfullscreen
            ></iframe>
        </div>
    @endif


    <!-- Add to contacts -->
    <a href="{{ url('/digitalCard/' . $user->dk_uuid . '/downloadVcf') }}" class="add-contact-btn">
        <i class="fa-solid fa-address-card"></i> Add to Contacts
    </a>


</a>

</div>

<script>
document.addEventListener('DOMContentLoaded', function () {

    const qrThumb   = document.getElementById('qrCode');
    const qrModal   = document.getElementById('qrModal');
    const qrModalImg= document.getElementById('qrModalImg');

    if (!qrThumb) {
        console.error("QR thumbnail not found (missing id='qrCode')");
        return;
    }

    qrThumb.addEventListener('click', openQrModal);

    function openQrModal() {
        qrModalImg.src = qrThumb.src;
        qrModal.classList.add('show');
        bindModalCloseHandlers();
    }

    function closeQrModal() {
        unbindModalCloseHandlers();
        qrModal.classList.remove('show');
    }

    function bindModalCloseHandlers() {
        qrModal.addEventListener('click', closeQrModal);
        document.addEventListener('keydown', escHandler);
        qrModal.addEventListener('wheel', gestureHandler, { passive: true });

        qrModal.addEventListener('touchmove', gestureHandler, { passive: true });
        qrModal.addEventListener('gesturestart', gestureHandler, { passive: true });

    }

    function unbindModalCloseHandlers() {
        qrModal.removeEventListener('click', closeQrModal);
        document.removeEventListener('keydown', escHandler);
        qrModal.removeEventListener('wheel', gestureHandler);

        qrModal.removeEventListener('touchmove', gestureHandler);
        qrModal.removeEventListener('gesturestart', gestureHandler);

    }

    function escHandler(e) { if (e.key === 'Escape') closeQrModal(); }
    function gestureHandler() { closeQrModal(); }

});
</script>

  <!-- Fullscreen QR Modal -->
  <div id="qrModal" class="qr-modal" aria-hidden="true">
      <img id="qrModalImg" alt="QR Code (fullscreen)">
  </div>
</body>
</html>