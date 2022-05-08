package com.example.classmanagerandroid.Screens.Class.Components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
import com.example.classmanagerandroid.Screens.Course.MainViewModelCourse
import com.example.classmanagerandroid.Screens.Register.bigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Settings.ViewModelSettings
import com.example.classmanagerandroid.Screens.Utils.isAlphanumeric
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Composable
fun editClass(
    editClass: MutableState<Boolean>,
    mainViewModelClass: MainViewModelClass
) {
    val context = LocalContext.current
    val selectedCourse = remember { mainViewModelClass.selectedClass}
    var textName by remember{ mutableStateOf(mainViewModelClass.selectedClass.name) }
    var nameError by remember { mutableStateOf(false) }
    var editImg by remember { mutableStateOf(false) }
    val messageNameClassError by remember { mutableStateOf("Debes usar caracteres alfanumérico") }

    var textDescription by remember{ mutableStateOf(mainViewModelClass.selectedClass.description) }
    var descriptionError by remember { mutableStateOf(false) }
    val messageDescriptionClassError by remember { mutableStateOf("Debes usar caracteres caracter alfanumérico") }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .GetContent()
    ) { uri: Uri? ->
        mainViewModelClass.classImg.value = uri
        editImg = true
    }

    Dialog(
        onDismissRequest = {
            editClass.value = false
        },
        properties = DialogProperties(

        ),
        content = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxHeight(0.7f),
                content = {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                        content = {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(mainViewModelClass.classImg.value)
                                        .scale(Scale.FILL)
                                        .transformations(CircleCropTransformation())
                                        .build()
                                ),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clickable {
                                        launcher.launch("image/*")
                                    }
                            )
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")

                        }
                    )
                    Spacer(modifier = Modifier.padding(3.dp))

                    bigTextFieldWithErrorMessage(
                        text = "Nombre de la clase",
                        value = textName,
                        onValueChange = { textName = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = messageNameClassError,
                        changeError = { nameError = it},
                        error = nameError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    bigTextFieldWithErrorMessage(
                        text = "Descipción de la clase",
                        value = textDescription,
                        onValueChange = { textDescription = it },
                        validateError = ::isAlphanumeric,
                        errorMessage = messageDescriptionClassError,
                        changeError = { descriptionError = it},
                        error = descriptionError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        content = {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(
                                    start = 10.dp,
                                    top = 6.dp,
                                    end = 10.dp,
                                    bottom = 6.dp
                                ),
                                onClick = {
                                    mainViewModelClass.selectedClass.name = textName
                                    mainViewModelClass.selectedClass.description = textDescription
                                      mainViewModelClass.updateCurrentClass(
                                          updateClass = mainViewModelClass.selectedClass,
                                          onFinishResult = {
                                              editClass.value = false
                                          }
                                      )
                                    if (editImg) {
                                        updateImages(
                                            imageUri = mainViewModelClass.classImg.value,
                                            context = context,
                                            mainViewModelClass = mainViewModelClass
                                        )
                                        editImg = false
                                    }
                                },
                                content = {
                                    Text(text = "Guardar cambios")
                                }
                            )

                        }
                    )
                }
            )
        }
    )
}




fun updateImages(
    imageUri: Uri?,
    context: Context,
    mainViewModelClass: MainViewModelClass
){


    if (imageUri != null) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://class-manager-58dbf.appspot.com/")
        val folderRef : StorageReference = storageRef.child("classes/")
        val photoRef : StorageReference = folderRef.child(mainViewModelClass.selectedClass.id)

        val desertRef = storageRef.child("classes/${mainViewModelClass.selectedClass.id}")
        desertRef.delete()
            .addOnCompleteListener {

                if(it.isComplete){
                    photoRef
                        .putFile(imageUri)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                mainViewModelClass.selectedClass.img =  "gs://class-manager-58dbf.appspot.com/classes/${mainViewModelClass.selectedClass.id}"
                                mainViewModelClass.updateCurrentClass(
                                    updateClass = mainViewModelClass.selectedClass,
                                    onFinishResult = {
                                        CurrentUser.getMyClasses( onFinished = {} )
                                        Toast.makeText(context,"La clase se ha acualizado correctamente",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            else{
                                Toast.makeText(context,"No se ha podido actualizar la clase",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

}