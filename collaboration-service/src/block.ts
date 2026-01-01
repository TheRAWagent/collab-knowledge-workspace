import { blockNodes } from "./utils.ts";
import type { ProseMirrorDoc, ProseMirrorNode } from "./ProseMirrorDoc.ts";

export type BlockRecord = {
  id: string              // blockId
  documentId: string
  parentId: string | null
  orderIndex: number
  type: string
  text: string | null
  attrs: Record<string, any>
}

export function extractBlocks(snapshot: ProseMirrorDoc, documentId: string) {
  const blocks: BlockRecord[] = []

  function walk(nodes: ProseMirrorNode[], parentBlockId: string | null) {
    let orderIndex = 0

    for (const node of nodes) {
      if (blockNodes.has(node.type)) {
        const blockId = node.attrs?.blockId
        if (!blockId) throw new Error('Missing blockId')

        const block: BlockRecord = {
          id: blockId,
          documentId,
          parentId: parentBlockId,
          orderIndex,
          type: node.type,
          text: extractText(node),
          attrs: extractAttrs(node),
        }

        blocks.push(block)
        orderIndex++

        // recurse with this block as parent
        if (node.content) {
          walk(node.content, blockId)
        }
      } else if (node.content) {
        // inline or container node → keep same parent
        walk(node.content, parentBlockId)
      }
    }
  }

  walk(snapshot.content, null)
  return blocks
}

function extractText(node: ProseMirrorNode): string | null {
  if (!node.content) return null

  let text = ''

  function collect(n: ProseMirrorNode) {
    if (n.type === 'text' && typeof n.text === 'string') {
      text += n.text
    }
    if (n.content) {
      n.content.forEach(collect)
    }
  }

  collect(node)
  return text.length ? text : null
}

function extractAttrs(node: ProseMirrorNode) {
  const attrs = { ...(node.attrs ?? {}) }
  delete attrs.blockId
  return attrs
}

