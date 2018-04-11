package com.interedes.agriculturappv3.productor.models.ventas.resports

import com.interedes.agriculturappv3.R
import java.util.*

class GenreDataFactory {

    companion object {

    fun makeGenres(): List<Genre> {
        return Arrays.asList<Genre>(makeRockGenre(),
                makeJazzGenre(),
                makeClassicGenre(),
                makeSalsaGenre(),
                makeBluegrassGenre())
    }



    fun makeRockGenre(): Genre {
        return Genre("Rock", makeRockArtists(), R.drawable.ic_electric_guitar)
    }



    fun makeRockArtists(): List<Artist> {
        val queen = Artist("Queen", true)
        val styx = Artist("Styx", false)
        val reoSpeedwagon = Artist("REO Speedwagon", false)
        val boston = Artist("Boston", true)

        return Arrays.asList<Artist>(queen, styx, reoSpeedwagon, boston)
    }

    fun makeJazzGenre(): Genre {
        return Genre("Jazz", makeJazzArtists(), R.drawable.ic_saxaphone)
    }


    fun makeJazzArtists(): List<Artist> {
        val milesDavis = Artist("Miles Davis", true)
        val ellaFitzgerald = Artist("Ella Fitzgerald", true)
        val billieHoliday = Artist("Billie Holiday", false)

        return Arrays.asList<Artist>(milesDavis, ellaFitzgerald, billieHoliday)
    }

    fun makeClassicGenre(): Genre {
        return Genre("Classic", makeClassicArtists(), R.drawable.ic_violin)
    }



    fun makeClassicArtists(): List<Artist> {
        val beethoven = Artist("Ludwig van Beethoven", false)
        val bach = Artist("Johann Sebastian Bach", true)
        val brahms = Artist("Johannes Brahms", false)
        val puccini = Artist("Giacomo Puccini", false)

        return Arrays.asList<Artist>(beethoven, bach, brahms, puccini)
    }

    fun makeSalsaGenre(): Genre {
        return Genre("Salsa", makeSalsaArtists(), R.drawable.ic_maracas)
    }



    fun makeSalsaArtists(): List<Artist> {
        val hectorLavoe = Artist("Hector Lavoe", true)
        val celiaCruz = Artist("Celia Cruz", false)
        val willieColon = Artist("Willie Colon", false)
        val marcAnthony = Artist("Marc Anthony", false)

        return Arrays.asList<Artist>(hectorLavoe, celiaCruz, willieColon, marcAnthony)
    }

    fun makeBluegrassGenre(): Genre {
        return Genre("Bluegrass", makeBluegrassArtists(), R.drawable.ic_banjo)
    }


    fun makeBluegrassArtists(): List<Artist> {
        val billMonroe = Artist("Bill Monroe", false)
        val earlScruggs = Artist("Earl Scruggs", false)
        val osborneBrothers = Artist("Osborne Brothers", true)
        val johnHartford = Artist("John Hartford", false)

        return Arrays.asList<Artist>(billMonroe, earlScruggs, osborneBrothers, johnHartford)
    }
    }
}