$(document).ready(function() {
  var table;
  table = $('#tblReservation')
    .DataTable({
      "order": [6, 'desc'],
      "paging": true,
      "ordering": true,
      "processing": true,
      "serverSide": true,
      "language": {
        url: '//cdn.datatables.net/plug-ins/1.10.20/i18n/French.json'
      },

      "columnDefs": [{
        "targets": -1,
        "data": null,
        "defaultContent": " <button class=\"btn btn-secondary pret \"> <i class=\"fas fa-check-circle\"></i> Prêt</button>"
      }],
      "ajax": {
        "url": "php/pullReservation.php",
        "dataSrc": "data",
        "type": "POST",
        "dataSrc": function(data) {
          //$("body").append(JSON.stringify(data));
          idRows = data;
          return data.data;
        }
      },
      "createdRow": function(row, data, dataIndex) {
        $(row).attr('id', idRows.data[dataIndex].id);
        if (data["8"] == -2) {
          $(row).addClass("bg-danger");
        }
      }
    });
});
