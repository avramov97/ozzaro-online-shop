$(document).ready(function() {

    var table = $('#example').DataTable();

    $(function () {
        $('#datetimepicker_modal').datetimepicker({
            format: 'L'
        });
    });

    $(function () {
        $('#datetimepicker_modal_create').datetimepicker({
            format: 'L'
        });
    });
} );




