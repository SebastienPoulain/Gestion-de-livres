$(document).ready(function() {

  $(document).on("click", ".acceptConcession", function() {


    id = $(this).closest('tr').attr('id');

    $("#idAccepter").val(id);


    $("#accepterModal").modal("show");

  });

  $(document).on("click", "#accepter", function() {

    id = $("#idAccepter").val();

    $.ajax({
      url: "php/accepterConcession.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("La concession a été validée avec succès");
          $("#accepterModal").modal("hide");
          $('#tblValidationConcession').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});