$(document).ready(function() {

  $(document).on("click", ".ajoutLocalisation", function() {

    var id = $(this).closest('tr').attr('id');

    $("#idLocalisation").val(id);


    $("#AjoutLocalisationModal").modal("show");

  });

  $(document).on("click", "#ajoutLocalisation", function() {

    var id = $("#idLocalisation").val();

    var localisation = $("#localisation").val();

    if (localisation == "") {
      alert("Vous devez rentrer une localisation");
    } else {

      $.ajax({
        url: "php/changerLocalisation.php",
        method: "POST",
        data: {
          id: id,
          localisation: localisation
        },
        success: function(data) {
          if (data == "success") {
            alert("La localisation a été changée avec succès");
            $("#AjoutLocalisationModal").modal("hide");
            $('#tblLocalisation').DataTable().ajax.reload();
          } else {
            alert(data);
          }
        }
      });
    }
  });

});