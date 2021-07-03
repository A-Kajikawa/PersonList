package com.s26.personlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.s26.personlist.databinding.ActivityEditPersonBinding

class EditPersonActivity : AppCompatActivity() {
    companion object{
        const val OLD_PERSON="delete"
        const val NEW_PERSON="update"
        const val UUID="uuid"
    }

    private lateinit var binding: ActivityEditPersonBinding

    //更新処理
    private val updateClickListener = View.OnClickListener {

        //遷移先で新しいデータを入力
        val input1 = binding.nameInput
        val name = binding.nameInput.text.toString()
        val input2 = binding.ageInput
        val inputAge = binding.ageInput.text.toString()

        //何も入力されない時の処理
        if (name.isEmpty() && inputAge.toString().isEmpty()) {
            input1.error = getString(R.string.message)
            input2.error = getString(R.string.message)
            return@OnClickListener
        }else if (name.isEmpty()) {
            input1.error = getString(R.string.message)
            return@OnClickListener
        } else if(inputAge.toString().isEmpty()){
            input2.error = getString(R.string.message)
            return@OnClickListener
        }

        val age = binding.ageInput.text.toString().toInt()
        val isMarried = binding.radio1.isChecked
        val data = Person(name, age, isMarried)
        val uuid = intent.getLongExtra(PersonListActivity.OUTPUT_UUID, Long.MAX_VALUE)

        val intent = Intent().apply {
            putExtra(NEW_PERSON,data)
            putExtra(UUID,uuid)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    //削除処理
    private val deleteClickListener = View.OnClickListener {
        val person = intent.getSerializableExtra(PersonListActivity.OUTPUT_PERSON)?:return@OnClickListener
        val uuid = intent.getLongExtra(PersonListActivity.OUTPUT_UUID, Long.MAX_VALUE)
        val intent = Intent().apply{
            putExtra(OLD_PERSON,person)
            putExtra(UUID,uuid)
        }
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    private val cancelButtonClickListener = View.OnClickListener {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.updateButton.setOnClickListener(updateClickListener)
        binding.deleteButton.setOnClickListener(deleteClickListener)
        binding.cancelButton.setOnClickListener(cancelButtonClickListener)

        setResult(RESULT_CANCELED)
        val person = intent.getSerializableExtra(PersonListActivity.OUTPUT_PERSON) ?: return

        if (person is Person) {
            binding.nameInput.setText(person.name)
            binding.ageInput.setText(person.age.toString())
            if (person.isMarried) binding.choiceMarried.check(binding.radio1.id) else binding.choiceMarried.check(binding.radio2.id)
        }
    }
}