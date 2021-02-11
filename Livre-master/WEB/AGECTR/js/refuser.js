$(document).ready(function() {

  $(document).on("click", ".deleteUser", function() {

    var nomUser = $(event.target)
      .closest("tr")
      .find($("td:nth-child(2)"))
      .text();

    $("#userName2").text(nomUser + " ?");

    id = $(this).closest('tr').attr('id');

    $("#idSupprimer").val(id);


    $("#supprimerModal").modal("show");

  });

  $(document).on("click", "#supprimer", function() {

    id = $("#idSupprimer").val();

      $.ajax({
        url: "php/refuser.php",
        method: "POST",
        data: {
          id: id
        },
        success: function(data) {
          if (data == "success") {
            alert("L'utilisateur a été supprimé avec succès");
            $("#supprimerModal").modal("hide");
            $('#tblValidationComptes').DataTable().ajax.reload();
          } else {
            alert(data);
          }
        }
      });
    }

  });

});
