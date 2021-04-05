package com.caunb163.mxh.ui.main.messenger.create_group

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.caunb163.data.datalocal.LocalStorage
import com.caunb163.domain.model.User
import com.caunb163.mxh.MainActivity
import com.caunb163.mxh.R
import com.caunb163.mxh.base.BaseDialogFragment
import com.caunb163.mxh.base.BaseFragment
import com.caunb163.mxh.state.State
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.koin.android.ext.android.inject

@Suppress("UNCHECKED_CAST")
class CreateGroupFragment : BaseDialogFragment(), UserOnClick {
    private val TAG = "CreateGroupFragment"
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var createBtn: MaterialButton
    private lateinit var edtName: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var progress: ProgressBar

    private val viewModel: CreateGroupViewModel by inject()
    private val localStorage: LocalStorage by inject()

    private lateinit var glide: RequestManager
    private lateinit var userAdapter: UserAdapter
    private lateinit var user: User
    private val list = mutableListOf<User>()
    private val arrUserId = mutableListOf<String>()
    private var timenow: Long = 0

    override fun getLayoutId(): Int = R.layout.fragment_create_group

    override fun initView(view: View) {
        toolbar = view.findViewById(R.id.creategroup_toolbar)
        recyclerView = view.findViewById(R.id.creategroup_recyclerView)
        createBtn = view.findViewById(R.id.creategroup_btn)
        edtName = view.findViewById(R.id.creategroup_edt_name)
        progressBar = view.findViewById(R.id.creategroup_progressbar)
        progress = view.findViewById(R.id.creategroup_progress)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        glide = Glide.with(this)
        glide.applyDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.image_default)
                .error(R.drawable.image_default)
        )

        user = localStorage.getAccount()!!
        arrUserId.add(user.userId)
        userAdapter = UserAdapter(glide, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = userAdapter
        }
    }

    override fun initListener() {
        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty() && arrUserId.size > 1) {
                        createBtn.isEnabled = true
                        createBtn.setBackgroundColor(resources.getColor(R.color.colorPost, null))
                    } else {
                        createBtn.isEnabled = false
                        createBtn.setBackgroundColor(resources.getColor(R.color.colorUnPost, null))
                    }
                }
            }
        })

        createBtn.setOnClickListener {
            timenow = System.currentTimeMillis()
            viewModel.createGroup(arrUserId, edtName.text.toString(), timenow)
        }
    }

    override fun initObserve() {
        viewModel.getAllUser()
        viewModel.stateUser.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoading()
                is State.Success<*> -> onSuccess(state.data as MutableList<User>)
                is State.Failure -> onFailure(state.message)
            }
        })

        viewModel.stateGroup.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> onLoadingCreateGroup()
                is State.Success<*> -> onSuccessCreateGroup()
                is State.Failure -> onFailure(state.message)
            }
        })
    }

    private fun onLoading() {
        progress.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun onLoadingCreateGroup() {
        createBtn.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun onSuccess(users: MutableList<User>) {
        progress.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        list.clear()
        list.addAll(users)
        list.remove(user)
        userAdapter.updateData(list)

    }

    private fun onSuccessCreateGroup() {
        createBtn.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        (activity as MainActivity).onBackPressed()
    }

    private fun onFailure(message: String) {
        progress.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
        createBtn.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        showToast(message)
    }

    override fun userOnClick(u: User) {
        if (u.ischeck) {
            arrUserId.add(u.userId)
        } else {
            if (arrUserId.contains(u.userId)) {
                arrUserId.remove(u.userId)
            }
        }
        if (arrUserId.size > 1 && edtName.text.isNotEmpty()) {
            createBtn.isEnabled = true
            createBtn.setBackgroundColor(resources.getColor(R.color.colorPost, null))
        } else {
            createBtn.isEnabled = false
            createBtn.setBackgroundColor(resources.getColor(R.color.colorUnPost, null))
        }
    }
}