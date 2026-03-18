<?php

namespace app\Mail;
 
use Illuminate\Bus\Queueable;
use Illuminate\Mail\Mailable;
use Illuminate\Mail\Mailables\Content;
use Illuminate\Queue\SerializesModels;
use Illuminate\Support\Facades\Mail;


/*
public function envelope(): Envelope
{
    return new Envelope(
        from: new Address('dontreply@mbtapp.com', 'MBT APP'),
        replyTo: [
            new Address('mbt_app@daimlertruck.com', 'MBT APP'),
        ],
        subject: 'MBT App - Yeni bir geribildirim!',
    );
}
*/

class Feedback {
    public string $text ;
    public string $userName;
}
class NewFeedback extends Mailable
{
    use Queueable, SerializesModels;
    public $someData;
    /**
     * Create a new message instance.
     */
    public function __construct($someData) {
        $this->someData = $someData;
    }
 
    /**
     * Get the message content definition.
     */
    public function content(): Content
    {
        return new Content(
            view: 'mail.new-feedback-view',
        );
    }
}