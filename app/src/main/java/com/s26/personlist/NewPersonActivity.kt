package com.s26.personlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.s26.personlist.databinding.ActivityNewPersonBinding

class NewPersonActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_PERSON_ADD="person"
    }

    private lateinit var binding: ActivityNewPersonBinding

    private  val addButtonClickListener = View.OnClickListener {
        val input1 = binding.nameInput
        val name =  binding.nameInput.text.toString()
        val input2 = binding.ageInput
        val inputAge =  binding.ageInput.text.toString()

        //何も入力されない時の処理
        if (name.isEmpty() && inputAge.isEmpty()) {
            input1.error = getString(R.string.message)
            input2.error = getString(R.string.message)
            return@OnClickListener
        }else if (name.isEmpty()) {
            input1.error = getString(R.string.message)
            return@OnClickListener
        } else if(inputAge.isEmpty()){
            input2.error = getString(R.string.message)
            return@OnClickListener
        }

        val age = binding.ageInput.text.toString().toInt()
        val isMarried = binding.radio1.isChecked
        val person = Person(name,age,isMarried)

        val intent = Intent().apply{
            putExtra(EXTRA_PERSON_ADD, person)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private val cancelButtonClickListener = View.OnClickListener {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.okButton.setOnClickListener(addButtonClickListener)
        binding.cancelButton.setOnClickListener(cancelButtonClickListener)

        setResult(RESULT_CANCELED)
    }
}