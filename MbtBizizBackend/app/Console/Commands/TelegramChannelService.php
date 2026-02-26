<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 1.09.2020
 * Time: 15:46
 */

namespace App\Console\Commands;


class TelegramChannelService
{
    private const BOT_API_TOKEN = "1087838331:AAH23lhP0UwcswAqLJJBQC2Kf5W98Bd-KuE";
    private const CHANNEL_ID = "-1001183353288";

    static function sendMessage($message)
    {
        $data = [
            'chat_id' => self::CHANNEL_ID,
            'text' => $message
        ];

        file_get_contents("https://api.telegram.org/bot" . self::BOT_API_TOKEN . "/sendMessage?" . http_build_query($data) . "&parse_mode=html");
    }
}