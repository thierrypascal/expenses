package xtracted.view

import java.awt.Cursor
import java.util.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xtracted.controller.Action
import xtracted.data.Attribute


@Composable
fun <A: Action> ActionIconStrip(trigger: (A) -> Unit, vararg actionGroup : List<A>){
    Row(       modifier = Modifier.height(IntrinsicSize.Max),
      verticalAlignment = Alignment.CenterVertically,
                content = { actionGroup.forEach { actionList ->
                               actionList.forEach { action ->  ActionIcon(trigger = trigger, action = action)  }

                               VerticalDivider()
                               }
                          })
}

@Composable
fun <A: Action> ActionButtonStrip(trigger: (A) -> Unit, actionGroup: List<A>, modifier: Modifier = Modifier) {
    Row(modifier             = modifier.fillMaxWidth(),
       horizontalArrangement = Arrangement.SpaceEvenly,
       verticalAlignment     = Alignment.CenterVertically,
       content               = { actionGroup.forEach {
                                     ActionButton(trigger = trigger, action = it)
                                   }
                                })

}

@Composable
fun <A: Action> ActionClickableText(trigger: (A) -> Unit, action: A){
    Text(text = action.name,
         color = Color.Blue,
         style = MaterialTheme.typography.body2,
         modifier = Modifier.padding(8.dp)
                            .cursor(if(action.enabled) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR)
                            .clickable(onClick = { trigger(action) })
        )
}

@Composable
fun <A: Action> ActionButton(trigger: (A) -> Unit, action: A){
    Button(modifier = Modifier.cursor(if(action.enabled) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR),
            onClick = { trigger(action) },
            enabled = action.enabled,
            content = { Text(action.name) })
}

@Composable
fun <A: Action> ActionIcon(trigger: (A) -> Unit, action: A){
    withTooltip(control = { IconButton(modifier = Modifier.cursor(if(action.enabled) Cursor.HAND_CURSOR else Cursor.DEFAULT_CURSOR),
                                       onClick = { trigger(action) },
                                       enabled = action.enabled,
                                       content = { if(action.icon != null) Icon(action.icon!!, action.name) else Text(action.name) }) },
                tooltip = { Text(text = action.name,
                                 fontSize = 12.sp,
                                 fontWeight = FontWeight.Light,
                                 modifier = Modifier.padding(5.dp)
                                ) }
               )
}


@Composable
fun<T: Any> AttributeField(attribute: Attribute<T>, onValueChange : (String) -> Unit, password: Boolean = false, modifier: Modifier){
    OutlinedTextField(modifier             = modifier,
                      value                = attribute.valueAsText,
                      isError              = !attribute.isValid,
                      visualTransformation = if(password) PasswordVisualTransformation() else VisualTransformation.None,
                      onValueChange        = onValueChange)
}

@Composable
fun VerticalDivider(){
    Box(modifier = Modifier.fillMaxHeight()
                           .padding(vertical = 14.dp)
                           .width(1.dp)
                           .background(MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)))
}


@Composable
fun<T> SelectionBox(currentSelectionTitle: String, allItems: List<T>, onClick: () -> Unit, onItemClick: (T) -> Unit) {
    var dropDownExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.width(150.dp)
        .padding(5.dp)
        .border(width = 0.5.dp, color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
        .background(Color.White, shape = RoundedCornerShape(8.dp))
        .handCursor()
        .clickable(indication = null,
                   interactionSource = remember { MutableInteractionSource() },
                   onClick = { onClick()
                       dropDownExpanded = !dropDownExpanded
                   })) {
        Text(text     = currentSelectionTitle,
             modifier = Modifier.align(Alignment.CenterStart)
                 .padding(start = 8.dp)
                 .matchParentSize())
        Icon(imageVector        = Icons.Filled.ArrowDropDown,
             contentDescription = "Arrow DropDown",
             modifier           = Modifier.padding(8.dp)
                 .size(20.dp, 20.dp)
                 .align(Alignment.CenterEnd),
             tint               = MaterialTheme.colors.onSurface)

            DropdownMenu(expanded         = dropDownExpanded,
                onDismissRequest = { dropDownExpanded = false },
                modifier         = Modifier.background(MaterialTheme.colors.surface)
            ) {
            if (allItems.isEmpty()) {
                Text(text     = "No items yet",
                    modifier = Modifier.padding(10.dp))
            } else {
                allItems.forEach {
                    DropdownMenuItem(
                        onClick = {
                            onItemClick(it)
                            dropDownExpanded = false
                        }) {
                        Text(text    = it.toString(),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun<T: Any> FormField(attribute: Attribute<T>, onValueChange: (String) -> Unit, captionWidth: Dp = 100.dp, password: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()){
        Text(text     = attribute.caption,
             modifier = Modifier.width(captionWidth))
        AttributeField(attribute     = attribute,
                       onValueChange = onValueChange,
                       password      = password,
                       modifier      = Modifier.weight(1.0f)
                           .background(Color.White))
    }
}

@Composable
fun BooleanFormField(attribute: Attribute<Boolean>, onValueChange: (Boolean) -> Unit, captionWidth: Dp = 100.dp, enabled: Boolean = true) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()) {
        Text(text     = attribute.caption,
             modifier = Modifier.width(captionWidth))

        Switch(checked = attribute.value!!,
               enabled = enabled,
               onCheckedChange = { onValueChange(it) }
              )
    }
}

@Composable
fun<T: Any> SelectionFormField(attribute: Attribute<T>, allItems: List<T>, onItemClick: (T) -> Unit, captionWidth: Dp = 100.dp){
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()) {
        Text(text     = attribute.caption,
             modifier = Modifier.width(captionWidth))

        SelectionBox(currentSelectionTitle = attribute.valueAsText,
                     allItems = allItems.filter { it != attribute.value },
                     onClick = {},
                     onItemClick = { onItemClick(it) },
                    )
    }
}

@Composable
fun<T : Any> ReadOnlyFormField(attribute: Attribute<T>, captionWidth: Dp = 100.dp) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()) {
        Text(text     = attribute.caption,
             modifier = Modifier.width(captionWidth))

        Text(attribute.valueAsText)
    }
}

@Composable
fun Toolbar(content: @Composable () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()
                           .shadow(2.dp)
                           .background(color = Color.LightGray)
                           .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.CenterVertically) {
        content()
    }
}

@Composable
fun Link(url: String, text: String = "read more...") {
    val uriHandler = LocalUriHandler.current
    Text(text = text,
         color = Color.Blue,
         style = MaterialTheme.typography.body2,
         modifier = Modifier.padding(8.dp)
                            .cursor(Cursor.HAND_CURSOR)
                            .clickable(onClick = { uriHandler.openUri(url) })
        )
}

@Composable
fun AlignRight(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()){
        Spacer(Modifier.weight(1.0f))
        content()
    }
}

@Composable
fun AlignLeftRight(content: @Composable () -> Unit){
    Row(modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        content()
    }
}

@Composable
fun ErrorMessage(message: String){
    Text(text  = message,
         color = Color.Red)
}

@Composable
fun VSpace(space: Dp) {
    Spacer(modifier = Modifier.height(space))
}

@Composable
fun VulcanSalute(size: TextUnit = 73.sp) {
    Text(text     = "\uD83D\uDD96",
         fontSize = 73.sp)
}

private val tooltipBackground = Color(255, 255, 210)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun withTooltip(control : @Composable () -> Unit, tooltip : @Composable () -> Unit){
    // see: https://github.com/JetBrains/compose-jb/tree/master/tutorials/Desktop_Components#tooltips
    TooltipArea(tooltip = { // composable tooltip content
        Surface(modifier = Modifier.shadow(4.dp),
                color    = tooltipBackground,
                shape    = RoundedCornerShape(4.dp),
                content  = tooltip
               ) },
                content =  control
               )
}

fun Modifier.cursor(cursorId: Int) : Modifier = pointerHoverIcon(PointerIcon(Cursor(cursorId)))

fun Modifier.handCursor() = cursor(Cursor.HAND_CURSOR)

val CH = Locale("de", "CH")

fun Number?.format(pattern: String, nullFormat : String = "?"): String {
    return if (null == this) nullFormat else pattern.format(CH, this)
}

