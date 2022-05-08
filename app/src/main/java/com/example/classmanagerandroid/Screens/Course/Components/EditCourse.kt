package com.example.classmanagerandroid.Screens.Course

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Screens.Class.MainViewModelClass
import com.example.classmanagerandroid.Screens.Register.bigTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.isAlphanumeric
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Composable
fun editCourse(
    onValueChangeGetInformation: (Boolean) -> Unit,
    mainViewModelCourse: MainViewModelCourse
) {
    val selectedCourse = remember { mainViewModelCourse.selectedCourse}
    var textName by remember{ mutableStateOf(mainViewModelCourse.selectedCourse.name) }
    var nameError by remember { mutableStateOf(false) }
    val messageNameClassError by remember { mutableStateOf("Debes usar caracteres alfanumérico") }
    var editImg by remember { mutableStateOf(false) }
    var context = LocalContext.current

    var textDescription by remember{ mutableStateOf(mainViewModelCourse.selectedCourse.description) }
    var descriptionError by remember { mutableStateOf(false) }
    val messageDescriptionClassError by remember { mutableStateOf("Debes usar caracteres caracter alfanumérico") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .GetContent()
    ) { uri: Uri? ->
        mainViewModelCourse.courseImg.value = uri
        editImg = true
    }

    Dialog(
        onDismissRequest = {
            onValueChangeGetInformation(false)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        content = {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(mainViewModelCourse.courseImg.value)
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
                        }
                    )
                    Spacer(modifier = Modifier.padding(3.dp))

                    bigTextFieldWithErrorMessage(
                        text = "Nombre del curso",
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
                        text = "Descipción del curso",
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
                                    mainViewModelCourse.selectedCourse.name = textName
                                    mainViewModelCourse.selectedCourse.description = textDescription

                                    if (editImg) {
                                        updateImages(
                                            context = context,
                                            imageUri = mainViewModelCourse.courseImg.value,
                                            mainViewModelCourse = mainViewModelCourse
                                        )
                                        editImg = false
                                    }
                                    mainViewModelCourse.updateCurrentCourse(
                                        updateCourse = mainViewModelCourse.selectedCourse,
                                        onFinishResult = {
                                            onValueChangeGetInformation(false)
                                        }
                                    )
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
    mainViewModelCourse: MainViewModelCourse
){


    if (imageUri != null) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://class-manager-58dbf.appspot.com/")
        val folderRef : StorageReference = storageRef.child("classes/")
        val photoRef : StorageReference = folderRef.child(mainViewModelCourse.selectedCourse.id)

        val desertRef = storageRef.child("classes/${mainViewModelCourse.selectedCourse.id}")
        desertRef.delete()
            .addOnCompleteListener {

                if(it.isComplete){
                    photoRef
                        .putFile(imageUri)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                mainViewModelCourse.selectedCourse.img =  "gs://class-manager-58dbf.appspot.com/classes/${mainViewModelCourse.selectedCourse.id}"
                                mainViewModelCourse.updateCurrentCourse(
                                    updateCourse =  mainViewModelCourse.selectedCourse,
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