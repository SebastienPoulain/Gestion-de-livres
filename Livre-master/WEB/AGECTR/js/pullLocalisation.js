$(document).ready(function() {
  var table;
  table = $('#tblLocalisation')
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
        "defaultContent": "<button class=\"btn btn-success ajoutLocalisation\"> <i class=\"fas fa-plus-circle\"> Ajouter localisation</i></button>"
      }, {
        "targets": 7,
        "data": 'customerPrice',
        "render": function(data, type, row) {
          return data + " $";
        }
      }],
      "ajax": {
        "url": "php/pullLocalisation.php",
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
      },
    });
});