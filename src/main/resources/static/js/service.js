$(document).ready(function(){
    $(' .nBtn, .table .eBtn').on('click', function(event){
        event.preventDefault();
        var href = $(this).attr('href');
        var text = $(this).text();
        if(text=='Edit') {

            $.get(href, function (service, status) {
                $('.myForm #serviceName').val(service.serviceName);
                $('.myForm #description').val(service.description);
                $('.myForm #price').val(service.price);
            });

            $('.myForm #exampleModal').modal();
        }else{
            $('.myForm #serviceName').val("");
            $('.myForm #description').val("");
            $('.myForm #price').val(0);
            $('.myForm #exampleModal').modal();
        }
    });

    $('.table .delBtn').on('click', function(event){
        event.preventDefault();
        var href = $(this).attr('href');
        $('#myModal #delRef').attr('href',href);
        $('#myModal').modal();
        });
});