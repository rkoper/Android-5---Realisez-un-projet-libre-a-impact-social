package com.so.dingbring

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.so.dingbring.databinding.FragmentImageBinding
import kotlinx.android.synthetic.main.fragment_image.*


class ImageFragment : Fragment(), UploadListener {

    private val repository= DataRepository()
    private val factory= InfoViewModelFactory(repository)
    private val viewModel by lazy { ViewModelProviders.of(requireActivity(),factory).get(InfoViewModel::class.java) }
    private lateinit var binding: FragmentImageBinding

    companion object {
        private lateinit var storage: FirebaseStorage
        private lateinit var firestore: FirebaseFirestore
        private lateinit var storageReference: StorageReference
        const val SELECT_IMAGE_CODE = 100
        private lateinit var photoUri: Uri

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_image, container, false)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        binding.finish.setOnClickListener {
            if(!(binding.editText2.text==null||binding.editText1.text.isNullOrEmpty())){
                onStarted()
                viewModel.upload(binding.editText2.text.toString(),binding.editText1.text.toString())
           //     viewModel.uploadListener=this
            }

            else{
                Toast.makeText(activity,"enter the details to confirm", Toast.LENGTH_SHORT).show()

            }

        }

        binding.back.setOnClickListener {
            it.findNavController().navigate(R.id.action_imageFragment_to_homeFragment)
        }


        return binding.root
    }



    override fun onStarted() {
        binding.progressBar.visibility= View.VISIBLE

    }

    override fun onSuccess() {
        binding.progressBar.visibility= View.GONE
        val intent= Intent(activity,MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onFailure(message: String) {

        binding.progressBar.visibility = View.GONE
        Toast.makeText(activity,"fail to upload ", Toast.LENGTH_SHORT).show()
    }

}
