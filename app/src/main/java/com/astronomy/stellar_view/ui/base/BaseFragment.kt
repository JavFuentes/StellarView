package com.astronomy.stellar_view.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Fragmento base abstracto que implementa el patrón ViewBinding de forma segura y reutilizable
 * para todos los fragmentos de la aplicación StellarView.
 *
 * Esta clase base proporciona:
 * - Gestión automática y segura del ciclo de vida de ViewBinding
 * - Prevención de memory leaks mediante limpieza automática
 * - Implementación consistente del patrón ViewBinding en toda la app
 * - Reducción de código boilerplate en fragmentos derivados
 *
 * Beneficios del patrón implementado:
 * - **Type Safety**: Acceso tipado y seguro a las vistas del layout
 * - **Null Safety**: Manejo automático de referencias nulas durante el ciclo de vida
 * - **Performance**: Eliminación de findViewById() y mejor rendimiento
 * - **Memory Safety**: Prevención automática de memory leaks
 *
 * Uso recomendado:
 * Todos los fragmentos de la aplicación deben extender esta clase base
 * para garantizar consistencia y mejores prácticas en el manejo de vistas.
 *
 * @param B El tipo de ViewBinding específico del fragmento que extiende esta clase.
 *          Debe ser una subclase de [ViewBinding] generada automáticamente por Android.
 *
 * Ejemplo de uso:
 * ```kotlin
 * @AndroidEntryPoint
 * class HomeFragment : BaseFragment<FragmentHomeBinding>() {
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         // Usar binding.nombreVista de forma segura
 *         binding.textTitle.text = "Mi título"
 *     }
 *
 *     override fun initBinding(
 *         inflater: LayoutInflater,
 *         container: ViewGroup?
 *     ) = FragmentHomeBinding.inflate(inflater, container, false)
 * }
 * ```
 */
abstract class BaseFragment<B : ViewBinding> : Fragment() {

    /**
     * Instancia privada del ViewBinding que mantiene las referencias a las vistas del layout.
     * Se inicializa en [onCreateView] y se limpia en [onDestroyView] para prevenir memory leaks.
     *
     * La visibilidad privada garantiza que solo se acceda a través del getter [binding]
     * que incluye validaciones de seguridad.
     */
    private var viewBinding: B? = null

    /**
     * Getter público y seguro para acceder al ViewBinding del fragmento.
     *
     * Proporciona acceso tipado a todas las vistas del layout de forma segura,
     * validando que el ViewBinding esté inicializado antes del acceso.
     *
     * **Importante**: Solo debe usarse entre [onCreateView] y [onDestroyView].
     * Acceder fuera de este rango resultará en una excepción.
     *
     * @return La instancia actual de ViewBinding del fragmento
     * @throws IllegalStateException Si se accede cuando viewBinding es null
     *         (antes de onCreateView o después de onDestroyView)
     */
    protected val binding: B
        get() = viewBinding
            ?: throw IllegalStateException(
                "ViewBinding is null. Ensure you're accessing it between onCreateView and onDestroyView."
            )

    /**
     * Método abstracto que debe ser implementado por cada fragmento derivado
     * para inicializar su ViewBinding específico.
     *
     * Este método encapsula la lógica de inflado del layout específico de cada fragmento,
     * manteniendo la implementación del ciclo de vida en la clase base.
     *
     * Implementación típica:
     * ```kotlin
     * override fun initBinding(
     *     inflater: LayoutInflater,
     *     container: ViewGroup?
     * ) = FragmentMiLayoutBinding.inflate(inflater, container, false)
     * ```
     *
     * @param inflater El [LayoutInflater] proporcionado por Android para inflar el layout
     * @param container El [ViewGroup] padre donde se insertará la vista (puede ser null)
     * @return Una instancia del ViewBinding específico del fragmento
     */
    protected abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): B

    /**
     * Método del ciclo de vida de Fragment que crea y configura la vista principal.
     *
     * Esta implementación:
     * 1. Inicializa el ViewBinding llamando a [initBinding]
     * 2. Almacena la referencia de forma segura
     * 3. Retorna la vista raíz para que Android la use
     *
     * Los fragmentos derivados generalmente no necesitan override este método,
     * sino usar [onViewCreated] para configuración adicional.
     *
     * @param inflater Objeto para inflar layouts en esta vista
     * @param container Grupo padre donde se insertará la vista (nullable)
     * @param savedInstanceState Estado guardado del fragmento (nullable)
     * @return La vista raíz del fragmento inflada mediante ViewBinding
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = initBinding(inflater, container)
        return binding.root
    }

    /**
     * Método del ciclo de vida de Fragment que limpia las referencias de vista.
     *
     * Esta implementación crucial:
     * 1. Llama al método padre para limpieza estándar
     * 2. Establece viewBinding a null para liberar referencias
     * 3. Previene memory leaks al romper referencias circulares
     *
     * **Crítico para prevención de memory leaks**:
     * El ViewBinding mantiene referencias a las vistas del layout. Si no se limpia
     * cuando el fragmento se destruye, estas referencias pueden causar memory leaks
     * especialmente en casos de rotación de pantalla o navegación frecuente.
     *
     * Los fragmentos derivados pueden override este método, pero DEBEN llamar
     * a super.onDestroyView() para mantener la limpieza automática.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}