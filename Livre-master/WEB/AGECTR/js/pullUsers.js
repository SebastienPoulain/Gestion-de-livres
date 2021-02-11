$(document).ready(function() {
  var table;
  table = $('#tblValidationComptes')
    .DataTable({
      "order": [5, 'desc'],
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
          "defaultContent": "<button class=\"btn btn-success acceptUser\"> <i class=\"fas fa-plus-circle\"> Accepter</i></button> <button  class=\"btn btn-danger deleteUser \"> <i class=\"fa fa-trash\"></i> Refuser</button>"
        }, {
          "targets": 1,
          "data": 'firstName',
          "render": function(data, type, row) {
            return data + " " + row[2];
          }
        },
        {
          "targets": 6,
          "data": 'inactive',
          "render": function(data, type, row) {
            if (data == 1)
              return "Inactif";
            else {
              return "Actif";
            }
          }
        },
        {
          "visible": false,
          "targets": [2]
        }
      ],
      "ajax": {
        "url": "php/pullUsers.php",
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