package br.edu.utfpr.delgrau.calculaimc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    /**
     * **IMPORTANTE**:
     * Normalmente, o correto é criar as instância assim que são declaradas já que o Kotlin é null safe.
     * A única forma de delcarar um item null é assim, deliberadamente usando `lateinit`
     */
    private lateinit var etPeso: EditText
    private lateinit var etAltura: EditText
    private lateinit var tvResultado: TextView
    private lateinit var btCalcular: Button
    private lateinit var btLimpar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /**
         * capturamos o padding inicial definido no XML (16dp) antes de aplicar os insets.
         * dessa forma, somamos o padding do XML com os insets das barras do sistema (status bar e navigation bar),
         * evitando que o padding interno da tela seja sobrescrito pelo Edge-to-Edge
         */
        val mainView = findViewById<android.view.View>(R.id.main)
        val initialPaddingLeft = mainView.paddingLeft
        val initialPaddingTop = mainView.paddingTop
        val initialPaddingRight = mainView.paddingRight
        val initialPaddingBottom = mainView.paddingBottom

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + initialPaddingLeft,
                systemBars.top + initialPaddingTop,
                systemBars.right + initialPaddingRight,
                systemBars.bottom + initialPaddingBottom
            )
            insets
        }

        /**
         * inicializamos as variáveis declaradas com `lateinit` aqui, especificamente depois
         * do `setContentView()`, por que só depois dessa parte os componentes do XML existem
         */
        etPeso = findViewById(R.id.etPeso)
        etAltura = findViewById(R.id.etAltura)
        tvResultado = findViewById(R.id.tvResultado)
        btCalcular = findViewById(R.id.btCalcular)
        btLimpar = findViewById(R.id.btLimpar)

        btCalcular.setOnClickListener {
            btCalcularOnClick()
        }

        btLimpar.setOnClickListener {
            btLimparOnCLick()
        }

        btCalcular.setOnLongClickListener {
            Toast.makeText(this, "Botão para calcular o IMC", Toast.LENGTH_SHORT).show()
            true
        }

        btLimpar.setOnLongClickListener {
            Toast.makeText(this, "Botão para limpar os campos de texto", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun btLimparOnCLick() {
        etPeso.text.clear()
        etAltura.text.clear()
        tvResultado.text = "0.0"
        etPeso.requestFocus()
    }

    private fun btCalcularOnClick() {
        /**
         * aqui usamos o elvis operator (?:) para fazer um fallback em caso de erro
         * ou seja: se o valor for null, ele já executa esse código, se não, segue normal
         */
        val peso = etPeso.text.toString().toDoubleOrNull()?: run {
            etPeso.error = "Campo peso deve ser informado"
            etPeso.requestFocus()
            return
        }
        val altura = etAltura.text.toString().toDoubleOrNull()?: run {
            etAltura.error = "Campo altura deve ser informado"
            etAltura.requestFocus()
            return
        }
        tvResultado.text = "%.2f".format(peso / altura.pow(2.0))
    }

}