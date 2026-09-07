const botaoNovaEspecie = document.getElementById("botaoNovaEspecie");
const modalNovaEspecie = document.getElementById("modalNovaEspecie");
const botaoFecharModal = document.getElementById("botaoFecharModal");
const formNovaEspecie = document.getElementById("formNovaEspecie");
const tabelaEspecies = document.getElementById("tabelaEspecies");
const campoBusca = document.getElementById("campoBusca");

botaoNovaEspecie.addEventListener("click", function () {
    modalNovaEspecie.style.display = "flex";
});

botaoFecharModal.addEventListener("click", function () {
    modalNovaEspecie.style.display = "none";
});

formNovaEspecie.addEventListener("submit", async function (event) {

    event.preventDefault();

    const especie = {
        nomePopular: document.getElementById("nomePopular").value,
        nomeCientifico: document.getElementById("nomeCientifico").value,
        grupo: document.getElementById("grupo").value,
        bioma: document.getElementById("bioma").value,
        nivelRisco: document.getElementById("nivelRisco").value,
        populacaoEstimada: Number(
            document.getElementById("populacaoEstimada").value
        )
    };

    try {

        const resposta = await fetch("/especies", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(especie)
        });

        if (!resposta.ok) {
            throw new Error("Não foi possível cadastrar a espécie.");
        }

        alert("Espécie cadastrada com sucesso!");

        formNovaEspecie.reset();
        modalNovaEspecie.style.display = "none";

        carregarEspecies();

    } catch (erro) {

        console.error(erro);
        alert("Erro ao cadastrar a espécie.");

    }

});

async function carregarEspecies(nomePopular = "") {

    try {

        let url = "/especies";

        if (nomePopular.trim() !== "") {
            url += `?nomePopular=${encodeURIComponent(nomePopular)}`;
        }

        const resposta = await fetch(url);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar as espécies.");
        }

        const especies = await resposta.json();

        tabelaEspecies.innerHTML = "";

        especies.forEach(function (especie) {

            const linha = document.createElement("tr");

            linha.innerHTML = `
                <td>${especie.nomePopular}</td>

                <td>${formatarBioma(especie.bioma)}</td>

                <td>${formatarGrupo(especie.grupo)}</td>

                <td>
                    <span class="risco">
                        ${formatarRisco(especie.nivelRisco)}
                    </span>
                </td>

                <td>
                    <button class="botao-ver-mais">
                        Ver mais
                    </button>
                </td>
            `;

            tabelaEspecies.appendChild(linha);

        });

    } catch (erro) {

        console.error(erro);

        tabelaEspecies.innerHTML = `
            <tr>
                <td colspan="5">
                    Erro ao carregar as espécies.
                </td>
            </tr>
        `;

    }

}

campoBusca.addEventListener("input", function () {
    carregarEspecies(campoBusca.value);
});

function formatarBioma(bioma) {

    const biomas = {
        AMAZONIA: "Amazônia",
        CAATINGA: "Caatinga",
        CERRADO: "Cerrado",
        MATA_ATLANTICA: "Mata Atlântica",
        PAMPA: "Pampa",
        PANTANAL: "Pantanal"
    };

    return biomas[bioma] || bioma;
}

function formatarGrupo(grupo) {

    const grupos = {
        AVE: "Ave",
        MAMIFERO: "Mamífero",
        REPTIL: "Réptil",
        ANFIBIO: "Anfíbio",
        PEIXE: "Peixe"
    };

    return grupos[grupo] || grupo;
}

function formatarRisco(risco) {

    const riscos = {
        CRITICO: "Crítico",
        ALTO: "Alto",
        MODERADO: "Moderado",
        BAIXO: "Baixo"
    };

    return riscos[risco] || risco;
}

carregarEspecies();