package com.astronomy.stellar_view.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 *  BaseFragment es una clase abstracta que otras clases de Fragment pueden extender.
 *  Proporciona una implementación de la vista base y las funciones de ciclo de vida del Fragment.
 *  Toma un parámetro de tipo genérico B que debe ser un tipo de ViewBinding.
 */

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    // Variable que mantendrá la instancia de la vista de enlace
    private var viewBinding: B? = null

    // Getter para viewBinding. Lanzará una excepción si se accede antes de que la vista
    // sea creada o después de que sea destruida
    protected val binding get() = viewBinding!!

    // Función abstracta que las subclases deben implementar para inicializar su enlace de vista
    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): B

    // Función de ciclo de vida del Fragment llamada cuando Android necesita la vista de este Fragment
    // Aquí es donde se crea la vista de enlace y se devuelve la vista raíz de la vista de enlace
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = initBinding(inflater, container)

        return binding.root
    }

    // Función de ciclo de vida del Fragment llamada cuando la vista de este Fragment está a punto de ser destruida
    // Aquí es donde limpiamos la vista de enlace para evitar fugas de memoria
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}
