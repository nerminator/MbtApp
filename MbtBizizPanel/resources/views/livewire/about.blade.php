    <div class="container-fluid" style="margin-top: 25px;">
        <div class="row content">
            @include('leftsidebar')
            <div class="col-md-9" style="margin-top: 15px;">
                <form wire:submit="save">
                    @csrf
                    <h3 style="margin-top: 10px;">About TR</h3>
                    <textarea id="aboutTRHtml" name="aboutTRHtml" wire:model="aboutTR" style="width:100%;margin-left: 12px;background: dimgray;
    height: 300px;">{{ $aboutTR }}</textarea>
                    <h3 style="margin-top: 10px;">About EN</h3>
                    <textarea id="aboutENHtml" name="aboutENHtml" wire:model="aboutEN" style="width:100%;margin-left: 12px;background: dimgray;
    height: 300px;">{{ $aboutEN }}</textarea>
                    <br>
                    <div class="col-md-3">
                        <br>
                        <button type="submit" class="col-md-12 btn btn-primary">Save</button>
                    </div>
                </form>

            </div>
        </div>
    </div>
