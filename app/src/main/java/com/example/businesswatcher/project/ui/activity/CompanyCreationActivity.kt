package com.example.businesswatcher.project.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.businesswatcher.R
import com.example.businesswatcher.project.App
import com.example.businesswatcher.project.models.Company
import com.example.businesswatcher.project.models.Customer
import com.example.businesswatcher.project.models.Group
import com.example.businesswatcher.project.models.Type
import com.example.businesswatcher.project.viewmodel.CompanyViewModel
import com.example.businesswatcher.project.viewmodel.CompanyViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.util.concurrent.ThreadLocalRandom

class CompanyCreationActivity : AppCompatActivity() {

    private val companyViewModel: CompanyViewModel by viewModels {
        CompanyViewModelFactory((application as App).companyRepository)
    }

    lateinit var name: String
    lateinit var type: Type
    lateinit var group: Group
    lateinit var adress: String
    var turnover: Int = 0
    lateinit var customers: List<Customer>
    lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_creation)
        setBackButton()
        setValidationButton()
    }

    private fun setBackButton() =
        findViewById<ImageButton>(R.id.back_button).setOnClickListener { quit() }

    private fun setValidationButton() {
        findViewById<ImageButton>(R.id.validate_company).setOnClickListener {
            //getFieldsValues()
            val company: Company = createCompany()
            saveCompanyInDataBase(company)


            quit()
        }
    }

    private fun quit() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun getFieldsValues() {
        name = findViewById<TextInputEditText>(R.id.name_editText).text.toString()
        //if (!isCorrectlyFilled(name))
        //type =
        //group
        adress = findViewById<TextInputEditText>(R.id.address_editText).text.toString() + " " +
                findViewById<TextInputEditText>(R.id.city_editText).text.toString() + " " +
                findViewById<TextInputEditText>(R.id.PC_editText).text.toString()

        turnover = findViewById<TextInputEditText>(R.id.turnover_editText).text.toString().toInt()
        //customers =

        description = findViewById<TextInputEditText>(R.id.description_editText).text.toString()


    }

    //fun isCorrectlyFilled(value: Any): Boolean{
    //    when(value){
    //        name -> if (name == "") Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show()
    //        else return true
    //        adress -> if (adress == "") Toast.makeText(this, "aa", Toast.LENGTH_SHORT).show()
    //        else return true
//
    //        else -> return false
    //    }
//
//
//
    //}

    private fun createCompany(): Company {

        val id: Long = ThreadLocalRandom.current().nextLong(0, 999999999)

        return Company(
            id,
            "a",
            "a",
            1.0,
            2.0,
            2,
            "a"
        )
    }

    private fun saveCompanyInDataBase(company: Company) = companyViewModel.insert(company)
}