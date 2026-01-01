import { type Node } from '@tiptap/core';

export const nodeWithId = (node: Node) => {
  return node.extend({
    addAttributes() {
      return {
        ...this.parent?.(),
        blockId: {
          default: null,
          parseHTML: element =>
            element.getAttribute('data-block-id'),

          renderHTML: attrs => {
            if (!attrs.blockId) {
              return {}
            }

            return {
              'data-block-id': attrs.blockId,
            }
          },
        },
      }
    },
  })
}