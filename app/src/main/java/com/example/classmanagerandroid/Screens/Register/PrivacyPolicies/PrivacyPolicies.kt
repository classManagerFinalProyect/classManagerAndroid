package com.example.classmanagerandroid.Screens.Register.PrivacyPolicies


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.classmanagerandroid.R
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.defaultTopBar

@Composable
fun MainPrivacyPolicies(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            defaultTopBar(
                title = "Políticas de privacidad",
                navigationContent = {
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
                },
                actionsContent = {}
            )
        },
        content = {
            LazyColumn(
                content = {
                    item {
                        textTitle(stringResource(R.string.privacyPoliciesTitle))
                    }
                    item {
                        textItem(stringResource(R.string.privacyPoliciesText1))
                    }
                    item {
                        textItem(stringResource(R.string.privacyPoliciesText2))
                    }
                    item {
                        textTitle(stringResource(R.string.logDataTitle))
                    }
                    item {
                        textItem(stringResource(R.string.logDataText1))
                    }
                    item {
                        textItem(stringResource(R.string.logDataText2))
                    }
                    item {
                        textTitle(stringResource(R.string.userInformationTitle))
                    }
                    item {
                        textItem(stringResource(R.string.userInformationText1))
                    }
                    item {
                        textItem(stringResource(R.string.userInformationText2))
                    }
                    item {
                        textItem(stringResource(R.string.userInformationText3))
                    }
                    item {
                        textItem(stringResource(R.string.userInformationText4))
                    }
                    item {
                        textTitle(stringResource(R.string.securityInformationTitle))
                    }
                    item {
                        textItem(stringResource(R.string.securityInformationText1))
                    }
                    item {
                        textItem(stringResource(R.string.securityInformationText2))
                    }
                    item {
                        textItem(stringResource(R.string.securityInformationText3))
                    }
                    item {
                        textTitle(stringResource(R.string.keepInformationTitle))
                    }
                    item {
                        textItem(stringResource(R.string.keepInformationText1))
                    }
                    item {
                        textItem(stringResource(R.string.keepInformationText2))
                    }
                    item {
                        textTitle(stringResource(R.string.internationalInformationTitle))
                    }
                    item {
                        textItem(stringResource(R.string.internationalInformationText))
                    }
                    item {
                        textTitle(stringResource(R.string.informationControllingTitle))
                    }
                    item {
                        textItem(stringResource(R.string.informationControllingText1))
                    }
                    item {
                        textItem(stringResource(R.string.informationControllingText2))
                    }
                    item {
                        textItem(stringResource(R.string.informationControllingText3))
                    }
                    item {
                        textItem(stringResource(R.string.informationControllingText4))
                    }
                    item {
                        textItem(stringResource(R.string.informationControllingText5))
                    }
                    item {
                        textTitle(stringResource(R.string.cookiesTitle))
                    }
                    item {
                        textItem(stringResource(R.string.cookiesText))
                    }
                    item {
                        textTitle(stringResource(R.string.limitsTitle))
                    }
                    item {
                        textItem(stringResource(R.string.limitsText))
                    }
                    item {
                        textTitle(stringResource(R.string.policyChangesTitle))
                    }
                    item {
                        textItem(stringResource(R.string.policyChangesText1))
                    }
                    item {
                        textItem(stringResource(R.string.policyChangesText2))
                    }
                    item {
                        textTitle(stringResource(R.string.contactUsTitle))
                    }
                    item {
                        textItem(stringResource(R.string.contactUsText1))
                    }
                    item {
                        Spacer(modifier = Modifier.padding(5.dp))
                        textItem(stringResource(R.string.contactUsName))
                    }
                    item {
                        textItem(stringResource(R.string.contactUsMail))
                        Spacer(modifier = Modifier.padding(5.dp))
                    }

                }
            )
        }
    )
}
