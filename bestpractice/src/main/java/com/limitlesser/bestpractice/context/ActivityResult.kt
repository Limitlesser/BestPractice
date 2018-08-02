package com.limitlesser.bestpractice.context

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?) {
    val isOk = resultCode == Activity.RESULT_OK
}

class ResultFragment : Fragment() {

    private val subjects = mutableMapOf<Int, PublishSubject<ActivityResult>>()

    private var code = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startActivityForResult(intent: Intent, options: Bundle? = null): Observable<ActivityResult> {
        val requestCode = code++
        startActivityForResult(intent, requestCode, options)
        return waitingActivityResult(requestCode)
    }

    fun waitingActivityResult(requestCode: Int): Observable<ActivityResult> {
        val subject = PublishSubject.create<ActivityResult>()
        subjects[requestCode] = subject
        return subject
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val subject = subjects.remove(requestCode)
        subject?.onNext(ActivityResult(requestCode, resultCode, data))
        subject?.onComplete()
    }

}

open class ResultActivity : AppCompatActivity() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        resultFragment.onActivityResult(requestCode, resultCode, data)
    }
}

private const val FRAGMENT_TAG = "ActivityResultFragment"

val FragmentActivity.resultFragment: ResultFragment
    get() {
        return supportFragmentManager?.findFragmentByTag(FRAGMENT_TAG) as ResultFragment?
                ?: {
                    val fragment = ResultFragment()
                    supportFragmentManager.beginTransaction().add(fragment, FRAGMENT_TAG)
                            .commitAllowingStateLoss()
                    supportFragmentManager.executePendingTransactions()
                    fragment
                }()
    }

fun FragmentActivity.startActivityForResult(intent: Intent, options: Bundle? = null) =
        resultFragment.startActivityForResult(intent, options)


fun FragmentActivity.waitingActivityResult(requestCode: Int) = resultFragment.waitingActivityResult(requestCode)