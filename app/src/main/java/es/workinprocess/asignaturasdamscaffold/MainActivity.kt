package es.workinprocess.asignaturasdamscaffold

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import es.workinprocess.asignaturasdamscaffold.clases.Clase
import es.workinprocess.asignaturasdamscaffold.clases.Clases
import es.workinprocess.asignaturasdamscaffold.ui.theme.AsignaturasDamScaffoldTheme
import java.io.IOException
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsignaturasDamScaffoldTheme(
                dynamicColor = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScaffoldApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldApp() {

    val jsonData: String = loadJSOM(LocalContext.current)
    val clases: Clases = Gson().fromJson(jsonData, Clases::class.java)

    //Creo que la m la puse de mutable. :)
    var mfecha by remember { mutableStateOf(LocalDate.now()) } //LocalDate

    //Buscamos la clase correspondiente al dia.
    var mclase by remember { mutableStateOf(clases.getClase(mfecha.toString())) } //Clase

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.inversePrimary,
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = clases.getCurso()
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.inversePrimary,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =  Modifier.fillMaxWidth()
                ){
                    IconButton(
                        onClick = {
                            //Decrementamos la fecha y buscamos la clase.
                            mfecha = mfecha.minusDays(1)
                            mclase = clases.getClase(mfecha.toString())
                            //Pintamos en la consola
                            println("Left Click $mfecha")
                            println(mclase)
                        }
                    ){
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = "Anterior",
                            tint = colorResource(id = R.color.white),
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Text( text = "malonsog9")

                    IconButton(
                        onClick = {
                            //Incrementamos la fecha y buscamos la clase
                            mfecha = mfecha.plusDays(1)
                            mclase = clases.getClase(mfecha.toString())
                            //Pintamos en la consola
                            println("Right Click $mfecha")
                            println(mclase)
                        },
                        content = {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Siguiente",
                                tint = colorResource(id = R.color.white),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if(mclase != null){
                TarjetaDeClase(mclase!!)
            }
            else{
                TarjetaNoClase(mfecha)
            }
        }
    }
}

/**
 * Información de la clase No usamos Modifier.
 * @param clase Clase que pintamos.
 */
@Composable
fun TarjetaDeClase(clase: Clase){

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxHeight(0.5f)
    ){
        //Pintamos la caja de la fecha
        val fecha = LocalDate.parse(clase.fecha)

        DateBox(fecha.dayOfMonth, monthName(fecha.monthValue))
    }

    Column {
        Text(
            text = clase.modulo,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .alpha(0.5f)
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                imageVector =  Icons.Rounded.Info,
                contentDescription = "info",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(0.75f)
            )
            Text(
                text = clase.getInfoMessage(),
                fontSize = 16.sp,
                modifier = Modifier.padding(start=4.dp, top=0.dp)
            )
        }
    }
}

/**
 * Componible con el contenido cuando no hay clase.
 */
@Composable
fun TarjetaNoClase(fecha:LocalDate){

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxHeight(0.5f)
    ){
        //Pintamos la caja de la fecha
        DateBox(fecha.dayOfMonth, monthName(fecha.monthValue))
    }

    Text(
        text = "No hay clase",
        fontSize = 32.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .alpha(0.5f)
    )

}

@Composable
fun DateBox(day: Int, month: String){
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .height(150.dp)
            .width(150.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(1f)
                .padding(bottom = 16.dp)
        ){
            Text(
                text = day.toString(),
                textAlign = TextAlign.Center,
                fontSize = 72.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.inversePrimary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxHeight(1f)
        ){
            Text(
                text = month,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.inversePrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )
        }
    }

}

/**
 * Metodo para cargar el JSON de las clases.
 * @return String
 */
private fun loadJSOM(context: Context): String {

    var tContents = ""

    try{
        val stream = context.assets.open("clases.json")
        val size = stream.available()
        val buffer = ByteArray(size)
        stream.read(buffer)
        stream.close()
        tContents = String(buffer)
    }catch (e: IOException){
        //Gestionamos el error
    }

    return tContents
}

/**
 * Devuelve el nombre del mes recibido como entero.
 * @param monthIntValue Numero del mes recibido como un entero
 * @return String con el nombre del mes.
 */
private fun monthName(monthIntValue: Int): String = arrayOf("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre", "Noviembre","Diciembre")[monthIntValue-1]