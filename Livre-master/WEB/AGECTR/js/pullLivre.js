$(document).ready(function() {
  var table;
  table = $('#tblLivre')
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
        "defaultContent": "<button class=\"btn btn-success acceptLivre\"> <i class=\"fas fa-plus-circle\"> Accepter</i></button> <button  class=\"btn btn-danger deleteLivre \"> <i class=\"fa fa-trash\"></i> Refuser</button>"
      }, {
        "mData": "IMAGE",
        "targets": 6,
        "data": "imgPath",
        "render": function(data, type, row) {
          var path = "http://206.167.140.56:8080/420617RI/Equipe_2/img/";
          return '<img width="100" height="100" src="' + path + data + '">';
        }
      }],
      "ajax": {
        "url": "php/pullLivre.php",
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