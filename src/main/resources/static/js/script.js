const botaoNovaEspecie = document.getElementById("botaoNovaEspecie");
const modalNovaEspecie = document.getElementById("modalNovaEspecie");
const botaoFecharModal = document.getElementById("botaoFecharModal");
const formNovaEspecie = document.getElementById("formNovaEspecie");

const tabelaEspecies = document.getElementById("tabelaEspecies");
const campoBusca = document.getElementById("campoBusca");

const modalVerEspecie = document.getElementById("modalVerEspecie");
const botaoFecharVerEspecie = document.getElementById("botaoFecharVerEspecie");
const botaoAtualizar = document.getElementById("botaoAtualizar");
const botaoDeletar = document.getElementById("botaoDeletar");

let especieSelecionada = null;

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
                    <button
                        class="botao-ver-mais"
                        data-id="${especie.id}">
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

tabelaEspecies.addEventListener("click", function (event) {
    if (!event.target.classList.contains("botao-ver-mais")) {
        return;
    }
    const id = event.target.dataset.id;
    abrirVerEspecie(id);
});

async function abrirVerEspecie(id) {

    try {

        const resposta = await fetch(`/especies/${id}`);

        if (!resposta.ok) {
            throw new Error("Não foi possível carregar a espécie.");
        }

        const especie = await resposta.json();

        especieSelecionada = especie;

        document.getElementById("verNomePopular").value = especie.nomePopular;
        document.getElementById("verNomeCientifico").value = especie.nomeCientifico;
        document.getElementById("verGrupo").value = especie.grupo;
        document.getElementById("verBioma").value = especie.bioma;
        document.getElementById("verNivelRisco").value = especie.nivelRisco;
        document.getElementById("verPopulacaoEstimada").value =
            especie.populacaoEstimada;

        sairDoModoEdicao();

        modalVerEspecie.style.display = "flex";

    } catch (erro) {

        console.error(erro);
        alert("Erro ao carregar os dados da espécie.");
    }
}

botaoFecharVerEspecie.addEventListener("click", function () {
    modalVerEspecie.style.display = "none";
    especieSelecionada = null;
});

botaoAtualizar.addEventListener("click", async function () {
    if (!especieSelecionada) {
        return;
    }

    if (botaoAtualizar.textContent === "Salvar") {
        await salvarAlteracoes();
        return;
    }

    entrarNoModoEdicao();
});

function entrarNoModoEdicao() {
    document.getElementById("verNomePopular").readOnly = false;
    document.getElementById("verNomeCientifico").readOnly = false;
    document.getElementById("verGrupo").disabled = false;
    document.getElementById("verBioma").disabled = false;
    document.getElementById("verNivelRisco").disabled = false;
    document.getElementById("verPopulacaoEstimada").readOnly = false;

    botaoAtualizar.textContent = "Salvar";
}

function sairDoModoEdicao() {
    document.getElementById("verNomePopular").readOnly = true;
    document.getElementById("verNomeCientifico").readOnly = true;
    document.getElementById("verGrupo").disabled = true;
    document.getElementById("verBioma").disabled = true;
    document.getElementById("verNivelRisco").disabled = true;
    document.getElementById("verPopulacaoEstimada").readOnly = true;

    botaoAtualizar.textContent = "Atualizar";
}

async function salvarAlteracoes() {

    const especieAtualizada = {

        nomePopular: document.getElementById("verNomePopular").value,

        nomeCientifico: document.getElementById("verNomeCientifico").value,

        grupo: document.getElementById("verGrupo").value,

        bioma: document.getElementById("verBioma").value,

        nivelRisco: document.getElementById("verNivelRisco").value,

        populacaoEstimada: Number(
            document.getElementById("verPopulacaoEstimada").value
        )
    };

    try {

        const resposta = await fetch(
            `/especies/${especieSelecionada.id}`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(especieAtualizada)
            }
        );

        if (!resposta.ok) {
            throw new Error("Não foi possível atualizar a espécie.");
        }

        const especie = await resposta.json();

        especieSelecionada = especie;

        sairDoModoEdicao();

        alert("Espécie atualizada com sucesso!");

        carregarEspecies();

    } catch (erro) {

        console.error(erro);
        alert("Erro ao atualizar a espécie.");
    }
}

botaoDeletar.addEventListener("click", async function () {

    if (!especieSelecionada) {
        return;
    }

    const confirmou = confirm(
        `Deseja realmente deletar a espécie "${especieSelecionada.nomePopular}"?`
    );

    if (!confirmou) {
        return;
    }

    try {
        const resposta = await fetch(
            `/especies/${especieSelecionada.id}`,
            {
                method: "DELETE"
            }
        );

        if (!resposta.ok) {
            throw new Error("Não foi possível deletar a espécie.");
        }

        alert("Espécie deletada com sucesso!");

        modalVerEspecie.style.display = "none";

        especieSelecionada = null;

        carregarEspecies();

    } catch (erro) {

        console.error(erro);
        alert("Erro ao deletar a espécie.");

    }
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