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
import com.example.classmanagerandroid.Screens.Register.PrivacyPolicies.Items.TextItem
import com.example.classmanagerandroid.Screens.Register.PrivacyPolicies.Items.TextTitle
import com.example.classmanagerandroid.Screens.ScreenComponents.TopBar.DefaultTopBar

@Composable
fun MainPrivacyPolicies(
    navController: NavController,
) {
    Scaffold(
        topBar = {
            DefaultTopBar(
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
                        TextTitle(stringResource(R.string.privacyPoliciesTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.privacyPoliciesText1))
                    }
                    item {
                        TextItem(stringResource(R.string.privacyPoliciesText2))
                    }
                    item {
                        TextTitle(stringResource(R.string.logDataTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.logDataText1))
                    }
                    item {
                        TextItem(stringResource(R.string.logDataText2))
                    }
                    item {
                        TextTitle(stringResource(R.string.userInformationTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.userInformationText1))
                    }
                    item {
                        TextItem(stringResource(R.string.userInformationText2))
                    }
                    item {
                        TextItem(stringResource(R.string.userInformationText3))
                    }
                    item {
                        TextItem(stringResource(R.string.userInformationText4))
                    }
                    item {
                        TextTitle(stringResource(R.string.securityInformationTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.securityInformationText1))
                    }
                    item {
                        TextItem(stringResource(R.string.securityInformationText2))
                    }
                    item {
                        TextItem(stringResource(R.string.securityInformationText3))
                    }
                    item {
                        TextTitle(stringResource(R.string.keepInformationTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.keepInformationText1))
                    }
                    item {
                        TextItem(stringResource(R.string.keepInformationText2))
                    }
                    item {
                        TextTitle(stringResource(R.string.internationalInformationTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.internationalInformationText))
                    }
                    item {
                        TextTitle(stringResource(R.string.informationControllingTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.informationControllingText1))
                    }
                    item {
                        TextItem(stringResource(R.string.informationControllingText2))
                    }
                    item {
                        TextItem(stringResource(R.string.informationControllingText3))
                    }
                    item {
                        TextItem(stringResource(R.string.informationControllingText4))
                    }
                    item {
                        TextItem(stringResource(R.string.informationControllingText5))
                    }
                    item {
                        TextTitle(stringResource(R.string.cookiesTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.cookiesText))
                    }
                    item {
                        TextTitle(stringResource(R.string.limitsTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.limitsText))
                    }
                    item {
                        TextTitle(stringResource(R.string.policyChangesTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.policyChangesText1))
                    }
                    item {
                        TextItem(stringResource(R.string.policyChangesText2))
                    }
                    item {
                        TextTitle(stringResource(R.string.contactUsTitle))
                    }
                    item {
                        TextItem(stringResource(R.string.contactUsText1))
                    }
                    item {
                        Spacer(modifier = Modifier.padding(5.dp))
                        TextItem(stringResource(R.string.contactUsName))
                    }
                    item {
                        TextItem(stringResource(R.string.contactUsMail))
                        Spacer(modifier = Modifier.padding(5.dp))
                    }

                }
            )
        }
    )
}
