package com.example.classmanagerandroid.Screens.Settings.MyAccount


import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.classmanagerandroid.Classes.CurrentUser
import com.example.classmanagerandroid.Screens.Settings.ViewModelSettings
import com.example.classmanagerandroid.data.network.AccessToDataBase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


@Composable
fun MainMyAccount(
    navController: NavController,
    viewModelSettings: ViewModelSettings
) {
    var context = LocalContext.current
    var (imageUri,onValueChangeImageUri) = remember { mutableStateOf<Uri?>(null) }

    val (userName,onValueChangeUserName) = remember { mutableStateOf(CurrentUser.currentUser.name) }
    val (changeName,onValueChangeChangeName) = remember { mutableStateOf(false) }

    val (userDescription,onValueChangeUserDescription) = remember { mutableStateOf(CurrentUser.currentUser.description) }
    val (changeDescription,onValueChangeChangeDescription) = remember { mutableStateOf(false) }

    if (changeName) {
        changeUserValue(
            onValueChangeChangeName = onValueChangeChangeName,
            value = userName,
            onValueChangeUserName = onValueChangeUserName,
            label = "Escribe tu nuevo nombre",
            onClickSave = {
                CurrentUser.currentUser.name = userName
                onValueChangeChangeName(false)
                CurrentUser.uploadCurrentUser(onFinished = {})
            }
        )
    }

    if (changeDescription) {
        changeUserValue(
            onValueChangeChangeName = onValueChangeChangeDescription,
            value = userDescription,
            onValueChangeUserName = onValueChangeUserDescription,
            label = "Escribe tu descripción",
            onClickSave = {
                CurrentUser.currentUser.description = userDescription
                onValueChangeChangeDescription(false)
                CurrentUser.uploadCurrentUser(onFinished = {})
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Perfil")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    )
                }
            )
        },
        content = {
            LazyColumn(
                content ={
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    PaddingValues(
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                ),
                            content = {
                                getImage(
                                    imageUri = imageUri,
                                    onValueChangeImageUri = onValueChangeImageUri
                                )
                            }
                        )
                    }

                    item {
                        Row(
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable {
                                            onValueChangeChangeName(true)
                                        },
                                    content = {
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = "Perfil"
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            content = {
                                                Text(text = CurrentUser.currentUser.name)
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit user name",
                                        )
                                    }
                                )
                            }
                        )
                    }
                    item {
                        Row(
                            content = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start ,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable {
                                            onValueChangeChangeDescription(true)

                                        },
                                    content = {
                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Descripción del perfil"
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        Column(
                                            modifier = Modifier.fillMaxWidth(0.9f),
                                            content = {
                                                Text(text = CurrentUser.currentUser.description)
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit user description",
                                        )
                                    }
                                )
                            }
                        )
                    }

                    if(imageUri != null) {
                        item {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        PaddingValues(
                                            start = 10.dp,
                                            end = 10.dp
                                        )
                                    ),
                                onClick = {
                                    updateImages(
                                        imageUri =  imageUri,
                                        context = context,
                                        viewModelSettings = viewModelSettings
                                    )
                                },
                                content = {
                                    Text(text = "Guardar nueva imágen")
                                }
                            )
                        }
                    }
                }
            )
        }
    )
}




@Composable
fun getImage(
    imageUri: Uri?,
    onValueChangeImageUri: (Uri?) -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .GetContent()) { uri: Uri? ->
        onValueChangeImageUri(uri)
        CurrentUser.myImg.value = uri
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = 10.dp,
                    bottom = 10.dp
                )
            ),
        contentAlignment = Alignment.Center,
        content = {
            Image(
                painter = rememberAsyncImagePainter(model = CurrentUser.myImg.value),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        launcher.launch("image/*")
                    },
                contentScale = ContentScale.Crop,
            )
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
    )
}

fun updateImages(
    imageUri: Uri?,
    context: Context,
    viewModelSettings: ViewModelSettings
){


    if (imageUri != null) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://class-manager-58dbf.appspot.com/")
        val folderRef : StorageReference  = storageRef.child("user/")
        val photoRef : StorageReference  = folderRef.child(CurrentUser.currentUser.id)

        val desertRef = storageRef.child("user/${CurrentUser.currentUser.id}")
        desertRef.delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context,"Se ha eliminado la imágen", Toast.LENGTH_SHORT).show()
                }

                if(it.isComplete){
                    photoRef
                        .putFile(imageUri)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                CurrentUser.currentUser.imgPath =  "gs://class-manager-58dbf.appspot.com/user/${CurrentUser.currentUser.id}"
                                viewModelSettings.updateCurrentUser(
                                    onFinished = {
                                        Toast.makeText(context,"El usuario se ha acualizado correctamente",Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                            else{
                                Toast.makeText(context,"No se ha podido actualizar el usuario correctamente",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

}