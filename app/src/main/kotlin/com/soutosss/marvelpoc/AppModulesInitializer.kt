package com.soutosss.marvelpoc

import com.soutosss.marvelpoc.shared.KoinInitializer
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

class AppModulesInitializer : KoinInitializer() {
    override fun createKoinModules(): List<Module> {
        return listOf(module { viewModel { HomeViewModel(get()) } })
    }
}