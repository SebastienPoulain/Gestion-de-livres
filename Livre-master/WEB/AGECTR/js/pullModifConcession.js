$(document).ready(function() {
  var table;
  table = $('#tblModifConcession')
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
          "defaultContent": "<button class=\"btn btn-success acceptConcession\"> <i class=\"fas fa-plus-circle\"> Accepter</i></button> <button  class=\"btn btn-danger deleteConcession \"> <i class=\"fa fa-trash\"></i> Refuser</button>"
        },
        {
          "targets": 7,
          "data": 'customerPrice',
          "render": function(data, type, row) {
            return data + " $";
          }
        }, {
          "searchable": false,
          "orderable": false,
          "targets": 9
        }, {
          "targets": 8,
          "data": 'modif_prix',
          "render": function(data, type, row) {
            return data + " $";
          }
        }
      ],
      "ajax": {
        "url": "php/pullModifConcession.php",
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