package com.interedes.agriculturappv3.asistencia_tecnica.modules.asistencia_tecnica_module.plagas

import com.interedes.agriculturappv3.asistencia_tecnica.models.TipoProducto
import com.interedes.agriculturappv3.asistencia_tecnica.models.plagas.TipoEnfermedad
import com.interedes.agriculturappv3.asistencia_tecnica.modules.asistencia_tecnica_module.plagas.events.PlagasEvent
import com.interedes.agriculturappv3.libs.EventBus
import com.interedes.agriculturappv3.libs.GreenRobotEventBus
import org.greenrobot.eventbus.Subscribe


class PlagaPresenter(var view: IPlaga.View?) : IPlaga.Presenter {
    var interactor: IPlaga.Interactor? = null
    var eventBus: EventBus? = null

    init {
        interactor = PlagaInteractor()
        eventBus = GreenRobotEventBus()
    }

    //region Métodos Interfaz
    override fun onCreate() {
        eventBus?.register(this)
    }

    override fun onDestroy() {
        eventBus?.unregister(this)
    }

    @Subscribe
    override fun onEventMainThread(plagasEvent: PlagasEvent?) {
        when (plagasEvent?.eventType) {
            PlagasEvent.READ_EVENT -> {
                val list_plagas = plagasEvent.mutableList as ArrayList<TipoEnfermedad>
                view?.setDialogListPlagas(list_plagas)
                //view?.setListPlagas(list_plagas)
            }
            PlagasEvent.SET_EVENT -> {
                val list_plagas = plagasEvent.mutableList as ArrayList<TipoEnfermedad>
                view?.setListPlagas(list_plagas)
            }

            PlagasEvent.ITEM_SELECT_PLAGA_EVENT -> {
                val plaga = plagasEvent.objectMutable as TipoEnfermedad
                val list_plagas = ArrayList<TipoEnfermedad>()
                list_plagas.add(plaga)
                view?.setListPlagas(list_plagas)
            }
            PlagasEvent.ITEM_EVENT -> {
                val tipo_producto = plagasEvent.objectMutable as TipoProducto
                view?.hideDialog(tipo_producto)
                view?.getPlagasByTipoProducto(tipo_producto.Id)
            }
            PlagasEvent.ITEM_OPEN_EVENT -> {
                val tipo_enfermedad = plagasEvent.objectMutable as TipoEnfermedad
                view?.verInsumos(tipo_enfermedad)
            }
        }
    }

    override fun getPlagasByTipoProducto(tipoProductoId: Long?) {
        interactor?.getPlagasByTipoProducto(tipoProductoId)
    }


    override fun setPlaga(tipoEnfermedadId: Long?) {
        interactor?.setPlaga(tipoEnfermedadId)
    }
    //endregion
}