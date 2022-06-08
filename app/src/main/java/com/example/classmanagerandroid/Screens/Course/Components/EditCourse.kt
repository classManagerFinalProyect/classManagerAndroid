package com.example.classmanagerandroid.Screens.Course.Components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.classmanagerandroid.Screens.Course.MainViewModelCourse
import com.example.classmanagerandroid.Screens.ScreenItems.Inputs.BigOutlineTextFieldWithErrorMessage
import com.example.classmanagerandroid.Screens.Utils.CommonErrors
import com.example.classmanagerandroid.Screens.Utils.isValidDescription
import com.example.classmanagerandroid.Screens.Utils.isValidName
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@Composable
fun EditCourse(
    onValueChangeGetInformation: (Boolean) -> Unit,
    mainViewModelCourse: MainViewModelCourse
) {
    var textName by remember{ mutableStateOf(mainViewModelCourse.selectedCourse.name) }
    var nameError by remember { mutableStateOf(false) }
    var editImg by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var textDescription by remember{ mutableStateOf(mainViewModelCourse.selectedCourse.description) }
    var descriptionError by remember { mutableStateOf(false) }

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
                    .background(MaterialTheme.colors.background)
                    .border(BorderStroke(1.dp,MaterialTheme.colors.onSurface))
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

                    BigOutlineTextFieldWithErrorMessage(
                        text = "Nombre del curso",
                        value = textName,
                        onValueChange = { textName = it },
                        validateError = { isValidName(it) },
                        errorMessage = CommonErrors.notValidName,
                        changeError = { nameError = it},
                        error = nameError,
                        mandatory = false,
                        KeyboardType = KeyboardType.Text,
                        enabled = true
                    )

                    BigOutlineTextFieldWithErrorMessage(
                        text = "Descripción del curso",
                        value = textDescription,
                        onValueChange = { textDescription = it },
                        validateError = { isValidDescription(it) },
                        errorMessage = CommonErrors.notValidDescription,
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
                                    if (isValidDescription(text = textDescription) && isValidName(text = textName)) {
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
                                    }
                                    else
                                        Toast.makeText(context,CommonErrors.incompleteFields,Toast.LENGTH_SHORT).show()

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
        val folderRef : StorageReference = storageRef.child("courses/")
        val photoRef : StorageReference = folderRef.child(mainViewModelCourse.selectedCourse.id)

        val desertRef = storageRef.child("courses/${mainViewModelCourse.selectedCourse.id}")
        desertRef.delete()
            .addOnCompleteListener {

                if(it.isComplete){
                    photoRef
                        .putFile(imageUri)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                mainViewModelCourse.selectedCourse.img =  "gs://class-manager-58dbf.appspot.com/courses/${mainViewModelCourse.selectedCourse.id}"
                                mainViewModelCourse.updateCurrentCourse(
                                    updateCourse =  mainViewModelCourse.selectedCourse,
                                    onFinishResult = {
                                        Toast.makeText(context,"El curso se ha acualizado correctamente",
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