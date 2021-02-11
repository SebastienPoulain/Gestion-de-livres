$(document).ready(function() {

  $(document).on("click", ".acceptUser", function() {

    var nomUser = $(event.target)
      .closest("tr")
      .find($("td:nth-child(2)"))
      .text();

    $("#userName").text(nomUser + " ?");

    id = $(this).closest('tr').attr('id');

    $("#idAccepter").val(id);


    $("#accepterModal").modal("show");

  });

  $(document).on("click", "#accepter", function() {

    id = $("#idAccepter").val();

    $.ajax({
      url: "php/accepter.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("L'utilisateur a été validé avec succès");
          $("#accepterModal").modal("hide");
          $('#tblValidationComptes').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});
